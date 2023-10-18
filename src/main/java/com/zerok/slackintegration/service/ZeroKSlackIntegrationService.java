package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.request.ZeroKInferencePublishRequest;
import com.zerok.slackintegration.model.response.DashboardResponse;

import java.io.IOException;
import java.util.List;

public interface ZeroKSlackIntegrationService {
    List<String> publishInferenceToSlack(ZeroKInferencePublishRequest zeroKInferencePublishRequest) throws SlackApiException, IOException;

    DashboardResponse fetchSlackIntegration(String userId, String orgId);

    void disableSlackIntegration(String userId, String orgId) throws SlackApiException, IOException;
}
