package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.entities.VizierCluster;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.model.ZkSlackButton;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.model.slack.SlackMessage;
import com.zerok.slackintegration.repository.SlackClientIntegrationRepository;
import com.zerok.slackintegration.repository.VizierClusterRepository;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import com.zerok.slackintegration.utils.SlackMessageBuilder;
import com.zerok.slackintegration.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ZeroKSlackIntegrationServiceImpl implements ZeroKSlackIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(ZeroKSlackIntegrationServiceImpl.class);

    private final App slackApp;

    private final SlackAppService slackAppService;

    private final SlackClientIntegrationRepository slackClientIntegrationRepository;

    private final VizierClusterRepository vizierClusterRepository;

    @Autowired
    public ZeroKSlackIntegrationServiceImpl(App slackApp, SlackAppService slackAppService, SlackClientIntegrationRepository slackClientIntegrationRepository, VizierClusterRepository vizierClusterRepository) {
        this.slackApp = slackApp;
        this.slackAppService = slackAppService;
        this.slackClientIntegrationRepository = slackClientIntegrationRepository;
        this.vizierClusterRepository = vizierClusterRepository;
    }

    @Override
    public List<String> publishInferenceToSlack(ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException {
        //write logic here for publishing the inference
        // exception handling
        try {
            //fetch bot token specific to clientId

            //TODO : uncomment after testing
////            //fetch org info from cluster id
////            Optional<VizierCluster> optionalVizierCluster = vizierClusterRepository.findVizierClusterById(UUID.fromString(zeroKIssueInference.getClusterId()));
////
////            if (!optionalVizierCluster.isPresent()) {
////                log.debug("org id is not present for the given cluster id : {} ", zeroKIssueInference.getClusterId());
////                return null;
////            }
//
//            String orgId = optionalVizierCluster.get().getOrgId().toString();
            String orgId = "zerok";
            //fetch client token
            Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

            if (!optionalSlackClientIntegration.isPresent()) {
                log.debug("Slack reposting for org not onboarded tp zk-slack app, orgId : {} ", orgId);
                return null;
            }

            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            //format the text in a good way
            String text = zeroKIssueInference.getInference();
            SlackMessage slackMessage = SlackMessageBuilder.createSlackInferenceMessage(zeroKIssueInference);
            String slackAccessToken = Utils.decodeFromBase64(slackClientIntegration.getClientAccessToken());
            //fetch client channels
            List<String> slackChannelList = slackAppService.getSlackChannelsWhereAppInstalled(slackClientIntegration);
            System.out.println("channels: + ");
            System.out.println(slackChannelList);
            List<String> successChannels = new ArrayList<>();
            List<String> failedChannels = new ArrayList<>();
            List<String> response = new ArrayList<>();
            //post inference to at least one channel
            slackChannelList.forEach(channelId -> {
                ChatPostMessageRequest chatPostMessageRequest = ChatPostMessageRequest.builder()
                        .channel(channelId)
                        .text(text)
                        .blocks(slackMessage.getBlocks())
                        .token(slackAccessToken)
                        .build();
                try {
                    ChatPostMessageResponse chatPostMessageResponse = slackApp.client().
                            chatPostMessage(chatPostMessageRequest);
                    if (chatPostMessageResponse.isOk()) {
                        successChannels.add(chatPostMessageResponse.getChannel());
                        response.add(chatPostMessageResponse.getMessage().getText());
                    } else {
                        failedChannels.add(chatPostMessageResponse.getChannel());
                    }
                } catch (IOException | SlackApiException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("success: + ");
            System.out.println(successChannels);
            System.out.println("failed: + ");
            System.out.println(failedChannels);
            return response;
        } catch (SlackApiException e) {
            //TODO :: throw error
            logger.error("Exception while making slack api call");
        } catch (Exception e) {
            //TODO :: throw error
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public SlackIntegrationFetchResponse fetchSlackIntegration(String userId, String orgId) {
        //fetch from DB along with status
        Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

        if (!optionalSlackClientIntegration.isPresent()) {
            return SlackIntegrationFetchResponse.builder()
                    .orgId(orgId)
                    .status(SlackIntegrationStatus.INSTALLATION_PENDING)
                    .addToSlack(ZkSlackButton.builder()
                            .actionUrl("/u/slack/integration/initiate")
                            .label(SlackIntegrationStatus.INSTALLATION_PENDING.getDisplayName())
                            .disable(false)
                            .httpMethod(HttpMethod.POST)
                            .build())
                    .userId(userId)
                    .build();

        }
        else{
            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            return SlackIntegrationFetchResponse.builder()
                    .orgId(orgId)
                    .status(slackClientIntegration.getStatus())
                    .addToSlack(ZkSlackButton.builder()
//                            .actionUrl("/u/slack/integration/initiate")
                            .label(slackClientIntegration.getStatus().getDisplayName())
                            .disable(true)
//                            .httpMethod(HttpMethod.POST)
                            .build())
                    .userId(userId)
                    .build();
        }
    }

    @Override
    public void disableSlackIntegration(String userId, String orgId) {

        Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

        if (!optionalSlackClientIntegration.isPresent()) {
            //TODO :: throw error
            log.debug("slack integration disable request for org whose integration is not present");
        }
        else{
            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            slackClientIntegration.setStatus(SlackIntegrationStatus.DISABLED);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
            slackClientIntegration.setUpdatedOn(currentTimestamp);
            slackClientIntegration.setUpdatedBy(userId);
            slackClientIntegrationRepository.save(slackClientIntegration);
        }
    }


    public void tempInference(String token) throws SlackApiException, IOException {
        //write logic here for publishing the inference
        // exception handling
//        try{
        //fetch bot token specific to clientId

//        String token = slackApp.config().getSingleTeamBotToken();

        ChatPostMessageRequest chatPostMessageRequest = ChatPostMessageRequest.builder()
                .channel("zerok")
                .text("i am ZeroK Slack Bot")
                .token(token)
                .build();
        ChatPostMessageResponse chatPostMessageResponse = slackApp.client().
                chatPostMessage(chatPostMessageRequest);
        chatPostMessageResponse.getMessage().getText();
//        }
//        catch (SlackApiException e){
//            logger.error("Exception while making slack api call");
//        }
//        catch (Exception e){
//            logger.error(e.getMessage());
//        }

    }
}
