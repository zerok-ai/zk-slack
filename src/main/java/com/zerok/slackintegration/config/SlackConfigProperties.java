package com.zerok.slackintegration.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlackConfigProperties {

    @Value("${slack.clientId}")
    private String clientId;

    @Value("${slack.clientSecret}")
    private String clientSecret;

    @Value("${slack.redirectUri}")
    private String redirectUri;

    @Value("${slack.scopes}")
    private List<String> appScopes;

    @Value("${slack.signingSecret}")
    private String signingSecret;


}
