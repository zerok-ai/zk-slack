package com.zerok.slackintegration.service.impl;

import com.slack.api.bolt.App;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.apps.AppsUninstallRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.response.apps.AppsUninstallResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.model.Conversation;
import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.exception.SlackUnInstallException;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlackAppServiceImpl implements SlackAppService {

    private final App slackApp;

    private final SlackConfigProperties slackConfigProperties;

    @Autowired
    public SlackAppServiceImpl(App slackApp, SlackConfigProperties slackConfigProperties) {
        this.slackApp = slackApp;
        this.slackConfigProperties = slackConfigProperties;
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

    @Override
    public void uninstallSlackAppFromWorkSpace(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException {

        AppsUninstallRequest appsUninstallRequest = AppsUninstallRequest.builder()
                .token(Utils.decodeFromBase64(slackClientIntegration.getClientAccessToken()))
                .clientId(slackConfigProperties.getClientId())
                .clientSecret(slackConfigProperties.getClientSecret())
                .build();
        AppsUninstallResponse appsUninstallResponse = slackApp.client().appsUninstall(appsUninstallRequest);

        if(!appsUninstallResponse.isOk()){
           //TODO throw Exception
            throw new SlackUnInstallException("Error while uninstalling slack from workspace for client",500,appsUninstallResponse.getError());
        }

    }


}
