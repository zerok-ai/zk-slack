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
//    in case of installed
    private ZkSlackButton view;
    private ZkSlackButton disconnect;
//    in case of installation pending
    private ZkSlackButton addToSlack;
//    in case of disabled installation
    private ZkSlackButton reInstall;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
