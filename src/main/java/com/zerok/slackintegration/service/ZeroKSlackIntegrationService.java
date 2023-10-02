package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface ZeroKSlackIntegrationService {
     String publishInferenceToSlack(ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException;
}
