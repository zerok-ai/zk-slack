package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.zerok.slackintegration.entities.SlackClientIntegration;

import java.io.IOException;
import java.util.List;

public interface SlackAppService {
    List<String> getSlackChannelsWhereAppInstalled(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException;
    void uninstallSlackAppFromWorkSpace(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException;
    AuthTestResponse fetchSlackWorkspaceAccessToken(String accessToken) throws SlackApiException, IOException;
    OAuthV2AccessResponse fetchAccessTokenAndIncomingWebhooksForIntegration(String code) throws SlackApiException, IOException;

}
