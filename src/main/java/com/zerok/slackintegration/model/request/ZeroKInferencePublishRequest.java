package com.zerok.slackintegration.model.request;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ZeroKInferencePublishRequest {
    @NonNull
    private String issueId;

    @NonNull
    private String incidentId;

    @NonNull
    private String issueTitle;

    @NonNull
    private String inference;

    @NonNull
    private String clusterId;

}
