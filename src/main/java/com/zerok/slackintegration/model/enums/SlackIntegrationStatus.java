package com.zerok.slackintegration.model.enums;

import lombok.Getter;

@Getter
public enum SlackIntegrationStatus {
    INSTALLED("Installed"),
    DISABLED("Disabled"),
    INSTALLATION_PENDING("Installation Pending");

    private final String displayName;

    SlackIntegrationStatus(String displayName) {
        this.displayName = displayName;
    }

}
