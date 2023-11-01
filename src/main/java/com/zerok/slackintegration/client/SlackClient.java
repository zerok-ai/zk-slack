package com.zerok.slackintegration.client;

import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIncomingWebhookEntity;
import com.zerok.slackintegration.model.slack.SlackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class SlackClient {

    private final WebClient slackWebClient;

    private final WebClient slackWebhookClient;

    private final SlackConfigProperties slackConfigProperties;

    @Autowired
    public SlackClient(@Qualifier("slackWebClient") WebClient slackWebClient,@Qualifier("slackWebhookClient") WebClient slackWebhookClient, SlackConfigProperties slackConfigProperties) {
        this.slackWebClient = slackWebClient;
        this.slackWebhookClient = slackWebhookClient;
        this.slackConfigProperties = slackConfigProperties;
    }

    public Mono<String> authenticateAndFetchAccessTokenWithSlack(String code) {
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

    public void publishInferenceToSlackWebhookIntegrationChannels(List<SlackClientIncomingWebhookEntity> slackClientIncomingWebhookEntityList, SlackMessage slackMessage){
        slackClientIncomingWebhookEntityList.forEach(entity-> {
            try{
                String response = slackWebhookClient.post()
                        .uri(entity.getSlackWebhookUrl())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(BodyInserters.fromValue(slackMessage))
                        .retrieve()
                        .bodyToMono(String.class).toString();
                log.info("sucessfully published the inference for org: {} to slack channel: {}",entity.getOrg(),entity.getSlackChannel());
            }
            catch (Exception e){
                log.error("error file publishing to webhook channel : {}",entity.getSlackChannel());
            }
        });
    }
}
