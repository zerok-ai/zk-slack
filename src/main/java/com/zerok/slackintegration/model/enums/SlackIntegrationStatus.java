package com.zerok.slackintegration.model.enums;

import lombok.Getter;

@Getter
public enum SlackIntegrationStatus {
    INSTALLED("Connected to Slack"),
    DISABLED("Disabled"),
    INSTALLATION_PENDING("Connect to Slack");

    private final String displayName;

    SlackIntegrationStatus(String displayName) {
        this.displayName = displayName;
    }

}
