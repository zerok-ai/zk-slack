package com.zerok.slackintegration.client.webclients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "slackWebClient")
    public WebClient slackWebClient() {
        return WebClient.builder()
                .baseUrl("https://slack.com")
                .build();
    }

    @Bean(name = "zkGptWebClient")
    public WebClient zkGptWebClient() {
        return  WebClient.builder()
                .baseUrl("https://zk-gpt.com")
                .build();
    }

    @Bean(name = "slackWebhookClient")
    public WebClient slackWebhookClient() {
        return  WebClient.builder()
                .build();
    }


}

