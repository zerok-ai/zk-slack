package com.zerok.slackintegration.service;

import com.zerok.slackintegration.entities.SlackClientIntegration;

public interface SlackClientTokenService {
    public void saveSlackToken(SlackClientIntegration slackToken);

    public SlackClientIntegration getSlackTokenByClientId(String clientId);

}
