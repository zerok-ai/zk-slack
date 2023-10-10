package com.zerok.slackintegration.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackOAuthRedirectRequest {
    private boolean ok;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    @JsonProperty("bot_user_id")
    private String botUserId;
    @JsonProperty("app_id")
    private String appId;
    private Team team;
    private Enterprise enterprise;
    @JsonProperty("authed_user")
    private AuthedUser authedUser;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Team {
        private String name;
        private String id;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Enterprise {
        private String name;
        private String id;

        // Getters and Setters (omitted for brevity)
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthedUser {
        private String id;
        private String scope;
        @JsonProperty("access_token")
        private String userAccessToken;
        @JsonProperty("token_type")
        private String userType;
    }
}
