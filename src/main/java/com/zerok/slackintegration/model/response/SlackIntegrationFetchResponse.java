package com.zerok.slackintegration.model.response;

import com.zerok.slackintegration.model.ZkSlackButton;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackIntegrationFetchResponse {

    private String orgId;
    private String userId;
    private SlackIntegrationStatus status; // Installed, DISABLED, Installation pending
    private String slackWorkspace;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}

