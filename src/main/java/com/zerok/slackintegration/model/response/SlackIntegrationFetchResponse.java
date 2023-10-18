package com.zerok.slackintegration.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SlackIntegrationFetchResponse implements ResponseEntity {

    @JsonProperty("org_id")
    private String orgId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("status")
    private SlackIntegrationStatus status; // Installed, DISABLED, Installation pending
    @JsonProperty("slack_workspace")
    private String slackWorkspace;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}

