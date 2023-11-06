package com.zerok.slackintegration.config;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {

    private final SlackConfigProperties slackConfigProperties;

    public SlackApp(SlackConfigProperties slackConfigProperties) {
        this.slackConfigProperties = slackConfigProperties;
    }

    @Bean
    public AppConfig loadOAuthConfig() {
        return AppConfig.builder()
                .clientId(slackConfigProperties.getClientId())
                .clientSecret(slackConfigProperties.getClientSecret())
                .signingSecret(slackConfigProperties.getSigningSecret())
                .scope(String.join(",", slackConfigProperties.getAppScopes()))
                .oauthInstallPath("/slack/install")
                .oauthRedirectUriPath("/slack/oauth_redirect")
                .build();
    }

    @Bean
    public App initSlackApp(AppConfig config) {
        App app = new App(config);
        if (config.getClientId() != null) {
            app.asOAuthApp(true);
        }
        return app;
    }

}