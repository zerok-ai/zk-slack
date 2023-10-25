package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.response.DashboardResponse;

import java.io.IOException;
import java.net.URI;

public interface SlackOAuthService {
    void fetchAndStoreClientAccessTokenFromTempToken(String clientTemporaryAccessToken,String state) throws SlackApiException, IOException;

    String exchangeAuthorizationCodeForAccessToken(String code);

    DashboardResponse createSlackOAuthRedirectionUri(String userId, String org);
}

