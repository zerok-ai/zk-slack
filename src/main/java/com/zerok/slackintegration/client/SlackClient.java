package com.zerok.slackintegration.client;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIncomingWebhookEntity;
import com.zerok.slackintegration.exception.SlackInferencePublishException;
import com.zerok.slackintegration.model.slack.SlackMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final Slack slackInstance;

    private final SlackConfigProperties slackConfigProperties;

    @Autowired
    public SlackClient(@Qualifier("slackWebClient") WebClient slackWebClient, SlackConfigProperties slackConfigProperties) {
        this.slackWebClient = slackWebClient;
        this.slackConfigProperties = slackConfigProperties;
        this.slackInstance = Slack.getInstance();
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
                Payload payload = Payload.builder()
                        .blocks(slackMessage.getBlocks())
                        .build();
                WebhookResponse response = slackInstance.send(entity.getSlackWebhookUrl(), payload);
                System.out.println(response); // WebhookResponse(code=200, message=OK, body=ok)
                log.info("sucessfully published the inference for org: {} to slack channel: {}",entity.getOrg(),entity.getSlackChannel());
            }
            catch (Exception e){
                log.error("error file publishing to webhook channel : {}",entity.getSlackChannel());
            }
        });
    }
}
