package com.zerok.slackintegration.client;

import com.zerok.slackintegration.config.SlackConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SlackClient {

    private final WebClient slackWebClient;

    @Autowired
    public SlackClient(@Qualifier("slackWebClient") WebClient slackWebClient) {
        this.slackWebClient = slackWebClient;
    }

    public Mono<String> authenticateAndFetchAccessTokenWithSlack(String code, SlackConfigProperties slackConfigProperties) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "client_id=" + slackConfigProperties.getClientId() +
                "&client_secret=" + slackConfigProperties.getClientSecret() +
                "&code=" + code +
                "&redirect_uri=" + slackConfigProperties.getRedirectUri();

        return slackWebClient.post()
                .uri("/api/oauth.v2.access")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(this::extractAccessToken);
    }

    private Mono<String> extractAccessToken(String responseBody) {
        if (responseBody == null) {
            return Mono.error(new RuntimeException("Failed to exchange authorization code for access token"));
        }
        // Assuming the response is in JSON format: {"access_token": "YOUR_ACCESS_TOKEN"}
        // Adapt this based on Slack API response format
        String accessToken = responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        return Mono.just(accessToken);
    }
}
