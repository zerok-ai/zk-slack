package com.zerok.slackintegration.service;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.entities.SlackClientIntegration;

import java.io.IOException;
import java.util.List;

public interface SlackAppService {
    List<String> getSlackChannelsWhereAppInstalled(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException;
    void uninstallSlackAppFromWorkSpace(SlackClientIntegration slackClientIntegration) throws SlackApiException, IOException;
}
