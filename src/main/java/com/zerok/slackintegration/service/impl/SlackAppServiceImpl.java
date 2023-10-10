package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlackAppServiceImpl implements SlackAppService {

    private final App slackApp;

    public SlackAppServiceImpl(App slackApp) {
        this.slackApp = slackApp;
    }

    @Override
    public List<String> getSlackChannelsWhereAppInstalled(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException {

        ConversationsListRequest conversationsListRequest = ConversationsListRequest.builder()
                .token(Utils.decodeFromBase64(slackClientIntegration.getClientAccessToken()))
                .excludeArchived(true)

                .build();
        ConversationsListResponse conversationsListResponse = slackApp.client().
                conversationsList(conversationsListRequest);

        if(conversationsListResponse.isOk()){
            return conversationsListResponse.getChannels().stream().filter(Conversation::isMember).map(Conversation::getId).collect(Collectors.toList());
        }
        else {
            //TODO: throw exception

        }
        return null;
    }
}
