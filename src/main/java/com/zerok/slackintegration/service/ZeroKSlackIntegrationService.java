package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface ZeroKSlackIntegrationService {
    List<String> publishInferenceToSlack(ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException;

    SlackIntegrationFetchResponse fetchSlackIntegration(String userId, String orgId);

    void disableSlackIntegration(String userId, String orgId);
}
