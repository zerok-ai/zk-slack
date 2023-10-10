package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.repository.SlackClientIntegrationRepository;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import com.zerok.slackintegration.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ZeroKSlackIntegrationServiceImpl implements ZeroKSlackIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(ZeroKSlackIntegrationServiceImpl.class);

    private final App slackApp;

    private final SlackAppService slackAppService;

    private final SlackClientIntegrationRepository slackClientIntegrationRepository;

    @Autowired
    public ZeroKSlackIntegrationServiceImpl(App slackApp, SlackAppService slackAppService, SlackClientIntegrationRepository slackClientIntegrationRepository) {
        this.slackApp = slackApp;
        this.slackAppService = slackAppService;
        this.slackClientIntegrationRepository = slackClientIntegrationRepository;
    }

    @Override
    public List<String> publishInferenceToSlack(ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException {
        //write logic here for publishing the inference
        // exception handling
        try {
            //fetch bot token specific to clientId

            //fetch client token
            SlackClientIntegration slackClientIntegration = slackClientIntegrationRepository.findByOrg(zeroKIssueInference.getOrg());

            //format the text in a good way
            String text = zeroKIssueInference.getInference();
            String slackAccessToken = Utils.decodeFromBase64(slackClientIntegration.getClientAccessToken());
            //fetch client channels
            List<String> slackChannelList = slackAppService.getSlackChannelsWhereAppInstalled(slackClientIntegration);
            System.out.println("channels: + ");
            System.out.println(slackChannelList);
            List<String> successChannels = new ArrayList<>();
            List<String> failedChannels = new ArrayList<>();
            List<String> resposne = new ArrayList<>();
            //post inference to at least one channel
            slackChannelList.forEach(channelId -> {
                ChatPostMessageRequest chatPostMessageRequest = ChatPostMessageRequest.builder()
                        .channel(channelId)
                        .text(text)
                        .token(slackAccessToken)
                        .build();
                try {
                    ChatPostMessageResponse chatPostMessageResponse = slackApp.client().
                            chatPostMessage(chatPostMessageRequest);
                    if (chatPostMessageResponse.isOk()) {
                        successChannels.add(chatPostMessageResponse.getChannel());
                        resposne.add(chatPostMessageResponse.getMessage().getText());
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
            return resposne;
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
        return null;
    }

    @Override
    public void disableSlackIntegration(String userId, String orgId) {
        return;
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
