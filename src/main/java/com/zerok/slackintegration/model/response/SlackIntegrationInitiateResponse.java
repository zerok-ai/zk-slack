package com.zerok.slackintegration.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackIntegrationInitiateResponse implements  ResponseEntity{
    @JsonProperty("org_id")
    private String orgId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("url")
    private String redirectUrl;
}
