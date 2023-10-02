package com.zerok.slackintegration;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackApp {

    // If you would like to run this app for a single workspace,
    // enabling this Bean factory should work for you.
//    @Bean
//    public AppConfig loadSingleWorkspaceAppConfig() {
//        return AppConfig.builder()
//                .singleTeamBotToken(System.getenv("SLACK_BOT_TOKEN"))
//                .signingSecret(System.getenv("SLACK_SIGNING_SECRET"))
//                .build();
//    }

    // If you would like to run this app for multiple workspaces,
    // enabling this Bean factory should work for you.
    @Bean
    public AppConfig loadOAuthConfig() {
        return AppConfig.builder()
//                .singleTeamBotToken("xoxb-2752528759122-5951411231171-AzfCF6UTJ4ruY0mieqK3aIdM")
//                .clientId(System.getenv("SLACK_CLIENT_ID"))
//                .clientSecret(System.getenv("SLACK_CLIENT_SECRET"))
//                .signingSecret(System.getenv("SLACK_SIGNING_SECRET"))
                .clientId("2752528759122.5974500920640")
                .clientSecret("5a73604214192016035cf5871e1932a9")
                .signingSecret("09b15216bb3371d41f72b4d4415b132f")
                .scope("app_mentions:read,channels:history,channels:read,chat:write")
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
        app.command("/hello", (req, ctx) -> {
            return ctx.ack(r -> r.text("Thanks!"));
        });
        return app;
    }

}