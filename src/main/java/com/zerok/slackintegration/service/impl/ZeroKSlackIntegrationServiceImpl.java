package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.entities.VizierCluster;
import com.zerok.slackintegration.model.request.ZeroKInferencePublishRequest;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import com.zerok.slackintegration.model.response.DashboardResponse;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.model.slack.SlackMessage;
import com.zerok.slackintegration.repository.SlackClientIntegrationRepository;
import com.zerok.slackintegration.repository.VizierClusterRepository;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import com.zerok.slackintegration.utils.SlackMessageBuilder;
import com.zerok.slackintegration.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final SlackConfigProperties slackConfigProperties;

    @Autowired
    public ZeroKSlackIntegrationServiceImpl(App slackApp, SlackAppService slackAppService, SlackClientIntegrationRepository slackClientIntegrationRepository, VizierClusterRepository vizierClusterRepository, SlackConfigProperties slackConfigProperties) {
        this.slackApp = slackApp;
        this.slackAppService = slackAppService;
        this.slackClientIntegrationRepository = slackClientIntegrationRepository;
        this.vizierClusterRepository = vizierClusterRepository;
        this.slackConfigProperties = slackConfigProperties;
    }

    @Override
    public List<String> publishInferenceToSlack(ZeroKInferencePublishRequest zeroKInferencePublishRequest) throws SlackApiException, IOException {
        //write logic here for publishing the inference
        // exception handling
        try {
            //fetch bot token specific to clientId

            //TODO : uncomment after testing
            //fetch org info from cluster id
            Optional<VizierCluster> optionalVizierCluster = vizierClusterRepository.findVizierClusterById(UUID.fromString(zeroKInferencePublishRequest.getClusterId()));

            if (optionalVizierCluster.isEmpty()) {
                log.debug("org id is not present for the given cluster id : {} ", zeroKInferencePublishRequest.getClusterId());
                return null;
            }

            String orgId = optionalVizierCluster.get().getOrgId().toString();
            //String orgId = "zerok";
            //fetch client token
            Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

            if (optionalSlackClientIntegration.isEmpty()) {
                log.debug("Slack reposting for org not onboarded tp zk-slack app, orgId : {} ", orgId);
                return null;
            }

            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            //format the text in a good way
            String text = zeroKInferencePublishRequest.getInference();
            String zeroKDashboardUrl = Utils.generateDashboardIssueUrl(slackConfigProperties.getZeroKDashboardUrl(), zeroKInferencePublishRequest);
            SlackMessage slackMessage = SlackMessageBuilder.generateSlackIntegrationMessage(zeroKInferencePublishRequest, zeroKDashboardUrl);
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
    public DashboardResponse fetchSlackIntegration(String userId, String orgId) {
        //fetch from DB along with status
        Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

        if (optionalSlackClientIntegration.isEmpty()) {
            return DashboardResponse.builder()
                    .payload(SlackIntegrationFetchResponse.builder()
                            .orgId(orgId)
                            .status(SlackIntegrationStatus.PENDING)
                            .userId(userId)
                            .build())
                    .build();

        } else {
            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            return DashboardResponse.builder()
                    .payload(SlackIntegrationFetchResponse.builder()
                            .orgId(orgId)
                            .status(slackClientIntegration.getStatus())
                            .slackWorkspace(slackClientIntegration.getSlackWorkspace())
                            .userId(userId)
                            .build())
                    .build();
        }
    }

    @Transactional
    @Override
    public void disableSlackIntegration(String userId, String orgId) throws SlackApiException, IOException {

        Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(orgId);

        if (optionalSlackClientIntegration.isEmpty()) {
            //TODO :: throw error
            log.debug("slack integration disable request for org whose integration is not present");
        } else {
            SlackClientIntegration slackClientIntegration = optionalSlackClientIntegration.get();
            slackClientIntegration.setStatus(SlackIntegrationStatus.DISABLED);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
            slackClientIntegration.setUpdatedOn(currentTimestamp);
            slackClientIntegration.setUpdatedBy(userId);
            //uninstall slack app from a workspace
            slackAppService.uninstallSlackAppFromWorkSpace(slackClientIntegration);
            slackClientIntegrationRepository.delete(slackClientIntegration);
            log.debug(String.format("Deleting slack integration for org: %s by userId: %s",orgId, userId));
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
