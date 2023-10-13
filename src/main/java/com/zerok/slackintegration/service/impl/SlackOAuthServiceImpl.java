package com.zerok.slackintegration.service.impl;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.client.SlackClient;
import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.entities.SlackClientOAuthState;
import com.zerok.slackintegration.exception.InvalidSlackClientOAuthStateReceived;
import com.zerok.slackintegration.exception.SlackIntegrationInitiateException;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import com.zerok.slackintegration.repository.SlackClientIntegrationRepository;
import com.zerok.slackintegration.repository.SlackClientOAuthStateRepository;
import com.zerok.slackintegration.service.SlackOAuthService;
import com.zerok.slackintegration.utils.Utils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SlackOAuthServiceImpl implements SlackOAuthService {

    private final SlackConfigProperties slackConfigProperties;
    private final SlackClient slackClient;
    private final SlackClientOAuthStateRepository slackClientOAuthStateRepository;

    private final SlackClientIntegrationRepository slackClientIntegrationRepository;

    private final ZeroKSlackIntegrationServiceImpl zeroKSlackIntegrationService;

    @Autowired
    public SlackOAuthServiceImpl(SlackConfigProperties slackConfigProperties, SlackClient slackClient, SlackClientOAuthStateRepository slackClientOAuthStateRepository, SlackClientIntegrationRepository slackClientIntegrationRepository, ZeroKSlackIntegrationServiceImpl zeroKSlackIntegrationService) {
        this.slackConfigProperties = slackConfigProperties;
        this.slackClient = slackClient;
        this.slackClientOAuthStateRepository = slackClientOAuthStateRepository;
        this.slackClientIntegrationRepository = slackClientIntegrationRepository;
        this.zeroKSlackIntegrationService = zeroKSlackIntegrationService;
    }

    public String exchangeAuthorizationCodeForAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "client_id=" + slackConfigProperties.getClientId() +
                "&client_secret=" + slackConfigProperties.getClientSecret() +
                "&code=" + code +
                "&redirect_uri=" + slackConfigProperties.getRedirectUri();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://slack.com/api/oauth.v2.access",
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Parse the response JSON and extract the access token
            // Assuming the response is in JSON format: {"access_token": "YOUR_ACCESS_TOKEN"}
            // Adapt this based on Slack API response format
            String responseBody = responseEntity.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Failed to exchange authorization code for access token");
            }
            return responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        } else {
            throw new RuntimeException("Failed to exchange authorization code for access token");
        }
    }

    @Transactional
    @Override
    public URI createSlackOAuthRedirectionUri(String userId, String org) {

        try {
            //check for deduplication
            Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrg(org);
            if(optionalSlackClientIntegration.isPresent()){
                //TODO :: throw error
                return null;
            }

            String stateOAuthKey = UUID.randomUUID().toString();

            //TODO : store this state in DB and status as pending , UI should send userId, org Id in input map
            SlackClientOAuthState slackClientOAuthState = SlackClientOAuthState.builder()
                    .createdBy(userId)
                    .org(org)
                    .stateOAuthKey(stateOAuthKey)
                    .build();

            slackClientOAuthStateRepository.save(slackClientOAuthState);


            return URI.create("https://slack.com/oauth/v2/authorize" + "?client_id=" + slackConfigProperties.getClientId() +
                    "&scope=" + String.join(",", slackConfigProperties.getAppScopes()) +
                    "&state=" + String.join(",", stateOAuthKey) +
                    "&redirect_uri=" + slackConfigProperties.getRedirectUri());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SlackIntegrationInitiateException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while creating the slack redirection URL");
        }
    }


    @Override
    @Transactional
    public void fetchAndStoreClientAccessTokenFromTempToken(String clientTemporaryAccessToken, String state) throws SlackApiException, IOException {

        try {
            Mono<String> clientAccessTokenMono = slackClient.authenticateAndFetchAccessTokenWithSlack(clientTemporaryAccessToken, slackConfigProperties);

            // Subscribe to the Mono to get the client access token
            String clientAccessToken = clientAccessTokenMono
                    .doOnNext(token -> {
                        System.out.println("Received client access token from Slack: " + token);
                    })
                    .doOnError(error -> {
                        System.err.println("Error while fetching client access token: " + error.getMessage());
                        //TODO :: throw error
                    })
                    .block();
            //store access token in slack client integration
            //fetch user and org from stateOAuthTable
            SlackClientOAuthState slackClientOAuthState = slackClientOAuthStateRepository.findSlackClientOAuthStatesByStateOAuthKey(state);

            //validate state
            validateSlackClientOAuthState(slackClientOAuthState);

            if (StringUtil.isNullOrEmpty(clientAccessToken)) {
                throw new SlackIntegrationInitiateException("", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while creating the slack redirection URL");
            }
            String encodedAccessToken = Utils.encodeToBase64(clientAccessToken);

            SlackClientIntegration slackClientIntegration = SlackClientIntegration.builder()
                    .clientAccessToken(encodedAccessToken)
//                  .slackChannel()
                    .status(SlackIntegrationStatus.INSTALLED)
                    .org(slackClientOAuthState.getOrg())
                    .createdBy(slackClientOAuthState.getCreatedBy())
                    .tokenType("bearer")
                    .updatedBy(slackClientOAuthState.getCreatedBy())
                    .build();

            slackClientIntegrationRepository.save(slackClientIntegration);
            //TODO : comment later on after testing
            zeroKSlackIntegrationService.tempInference(clientAccessToken);
        } catch (Exception ex) {
            throw new SlackIntegrationInitiateException("", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while creating the slack redirection URL");
        }
    }

    private void validateSlackClientOAuthState(SlackClientOAuthState slackClientOAuthState) {
        //write validation methods
        //check if slackClientOAuthState is null or not
        if (slackClientOAuthState == null || slackClientOAuthState.getOrg() == null || slackClientOAuthState.getCreatedBy() == null) {
            throw new InvalidSlackClientOAuthStateReceived("Invalid state key received from slack", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid state auth key received from slack");
        }
        //TODO: check for expiry and throw exception accordingly
    }


}
