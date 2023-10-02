package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZeroKSlackIntegrationServiceImpl implements ZeroKSlackIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(ZeroKSlackIntegrationServiceImpl.class);


    private final App slackApp;

    @Autowired
    public ZeroKSlackIntegrationServiceImpl(App slackApp) {
        this.slackApp = slackApp;
    }

    @Override
    public String publishInferenceToSlack(ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException {
        //write logic here for publishing the inference
//        try{
            System.out.println("i am here printing zeroKIssueInference");
            System.out.println(zeroKIssueInference);
            String token = slackApp.config().getSingleTeamBotToken();

            System.out.println("Token : " + token);
            ChatPostMessageRequest chatPostMessageRequest = ChatPostMessageRequest.builder()
                    .channel("temp")
                    .text("i am ZeroK Slack Bot")
                    .token(token)
                    .build();
        ChatPostMessageResponse chatPostMessageResponse = slackApp.client().
                    chatPostMessage(chatPostMessageRequest);
        return chatPostMessageResponse.getMessage().getText();
//        }
//        catch (SlackApiException e){
//            logger.error("Exception while making slack api call");
//        }
//        catch (Exception e){
//            logger.error(e.getMessage());
//        }

    }
}
