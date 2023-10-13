package com.zerok.slackintegration.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ZeroKIssueInference {
    private String issueId;

    private String incidentId;

    private String inference;

    private String issueTimestamp;

    private String clusterId;

    private String issueUrl;
}
