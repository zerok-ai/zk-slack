package com.zerok.slackintegration.service.impl;

import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.zerok.slackintegration.config.SlackConfigProperties;
import com.zerok.slackintegration.entities.SlackClientIncomingWebhookEntity;
import com.zerok.slackintegration.entities.SlackClientIntegration;
import com.zerok.slackintegration.entities.SlackClientOAuthState;
import com.zerok.slackintegration.exception.InvalidSlackClientOAuthStateReceived;
import com.zerok.slackintegration.exception.SlackIntegrationInitiateException;
import com.zerok.slackintegration.model.enums.SlackIntegrationStatus;
import com.zerok.slackintegration.model.response.DashboardResponse;
import com.zerok.slackintegration.model.response.SlackIntegrationInitiateResponse;
import com.zerok.slackintegration.repository.SlackClientIncomingWebhookRepository;
import com.zerok.slackintegration.repository.SlackClientIntegrationRepository;
import com.zerok.slackintegration.repository.SlackClientOAuthStateRepository;
import com.zerok.slackintegration.service.SlackAppService;
import com.zerok.slackintegration.service.SlackOAuthService;
import com.zerok.slackintegration.utils.Utils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SlackOAuthServiceImpl implements SlackOAuthService {

    private final SlackConfigProperties slackConfigProperties;
    private final SlackClientOAuthStateRepository slackClientOAuthStateRepository;

    private final SlackClientIntegrationRepository slackClientIntegrationRepository;

    private final SlackClientIncomingWebhookRepository slackClientIncomingWebhookRepository;

    private final SlackAppService slackAppService;

    @Autowired
    public SlackOAuthServiceImpl(SlackConfigProperties slackConfigProperties, SlackClientOAuthStateRepository slackClientOAuthStateRepository, SlackClientIntegrationRepository slackClientIntegrationRepository, SlackClientIncomingWebhookRepository slackClientIncomingWebhookRepository, SlackAppService slackAppService) {
        this.slackConfigProperties = slackConfigProperties;
        this.slackClientOAuthStateRepository = slackClientOAuthStateRepository;
        this.slackClientIntegrationRepository = slackClientIntegrationRepository;
        this.slackClientIncomingWebhookRepository = slackClientIncomingWebhookRepository;
        this.slackAppService = slackAppService;
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


    @Override
    public DashboardResponse createSlackOAuthRedirectionUri(String userId, String org) {

        try {
            //check for deduplication
            Optional<SlackClientIntegration> optionalSlackClientIntegration = slackClientIntegrationRepository.findSlackClientIntegrationByOrgAndStatus(org, SlackIntegrationStatus.INSTALLED);
            if (optionalSlackClientIntegration.isPresent()) {
                throw new SlackIntegrationInitiateException("Active slack integration already exists for client", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Active slack integration already exists for client");
            }

            String stateOAuthKey = UUID.randomUUID().toString();

            //TODO : store this state in DB and status as pending , UI should send userId, org Id in input map
            SlackClientOAuthState slackClientOAuthState = SlackClientOAuthState.builder()
                    .createdBy(userId)
                    .org(org)
                    .stateOAuthKey(stateOAuthKey)
                    .build();

            slackClientOAuthStateRepository.save(slackClientOAuthState);


            URI slackOAuthRedirectionUri = URI.create("https://slack.com/oauth/v2/authorize" + "?client_id=" + slackConfigProperties.getClientId() +
                    "&scope=" + String.join(",", slackConfigProperties.getAppScopes()) +
                    "&state=" + String.join(",", stateOAuthKey) +
                    "&redirect_uri=" + slackConfigProperties.getRedirectUri());

            return DashboardResponse.builder().payload(SlackIntegrationInitiateResponse.builder().redirectUrl(slackOAuthRedirectionUri.toString()).orgId(org).userId(userId).build()).build();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SlackIntegrationInitiateException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while creating the slack redirection URL");
        }
    }


    @Override
    @Transactional
    public void fetchAndStoreClientAccessTokenFromTempToken(String clientTemporaryAccessToken, String state) throws SlackApiException, IOException {

        try {
//            Mono<String> clientAccessTokenMono = slackClient.authenticateAndFetchAccessTokenWithSlack(clientTemporaryAccessToken);

            OAuthV2AccessResponse oAuthV2AccessResponse = slackAppService.fetchAccessTokenAndIncomingWebhooksForIntegration(clientTemporaryAccessToken);

            String clientAccessToken = oAuthV2AccessResponse.getAccessToken();

            log.info("Successfully fetched Org's access token integrated with team: {}",oAuthV2AccessResponse.getTeam().getName());

//            System.out.println("clientAccessTokenMono" + clientAccessTokenMono.toString());
//
//            // Subscribe to the Mono to get the client access token
//            String clientAccessToken = clientAccessTokenMono
//                    .doOnNext(token -> {
//                        System.out.println("Received client access token from Slack: " + token);
//                    })
//                    .doOnError(error -> {
//                        System.err.println("Error while fetching client access token: " + error.getMessage());
//                        //TODO :: throw error
//                    })
//                    .doFinally(signalType -> {
//                        // This block will be executed whether the Mono completes or errors out
//                        System.out.println("Process completed with signal: " + signalType);
//                    })
//                    .block();
            //store access token in slack client integration
            //fetch user and org from stateOAuthTable
            Optional<SlackClientOAuthState> optionalSlackClientOAuthState = slackClientOAuthStateRepository.findSlackClientOAuthStatesByStateOAuthKey(state);

            //validate state
            validateSlackClientOAuthState(optionalSlackClientOAuthState);

            SlackClientOAuthState slackClientOAuthState = optionalSlackClientOAuthState.get();

            if (StringUtil.isNullOrEmpty(clientAccessToken)) {
                throw new SlackIntegrationInitiateException("", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while creating the slack client access tokenL");
            }

            AuthTestResponse authTestResponse = slackAppService.fetchSlackWorkspaceAccessToken(clientAccessToken);

            if(!authTestResponse.isOk()){
                throw new SlackIntegrationInitiateException("", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid access token generated");
            }

            String encodedAccessToken = Utils.encodeToBase64(clientAccessToken);

            SlackClientIntegration slackClientIntegration = SlackClientIntegration.builder()
                    .clientAccessToken(encodedAccessToken)
                    .status(SlackIntegrationStatus.INSTALLED)
                    .org(slackClientOAuthState.getOrg())
                    .createdBy(slackClientOAuthState.getCreatedBy())
                    .tokenType("bearer")
                    .slackUserId(authTestResponse.getUserId())
                    .slackTeamId(authTestResponse.getTeamId())
                    .slackWorkspace(authTestResponse.getTeam())
                    .updatedBy(slackClientOAuthState.getCreatedBy())
                    .build();

            slackClientIntegrationRepository.save(slackClientIntegration);
            //store  incoming webhooks as well
            //TODO:: add optional in each of below for NPE
            SlackClientIncomingWebhookEntity slackClientIncomingWebhookEntity = SlackClientIncomingWebhookEntity.builder()
                    .slackWebhookUrl(oAuthV2AccessResponse.getIncomingWebhook().getUrl())
                    .slackChannel(oAuthV2AccessResponse.getIncomingWebhook().getChannel())
                    .slackTeamId(authTestResponse.getTeamId())
                    .slackChannelId(oAuthV2AccessResponse.getIncomingWebhook().getChannelId())
                    .slackWebhookConfigurationUrl(oAuthV2AccessResponse.getIncomingWebhook().getConfigurationUrl())
                    .org(slackClientOAuthState.getOrg())
                    .createdBy(slackClientOAuthState.getCreatedBy())
                    .build();
            slackClientIncomingWebhookRepository.save(slackClientIncomingWebhookEntity);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new SlackIntegrationInitiateException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
    }

    private void validateSlackClientOAuthState(Optional<SlackClientOAuthState> optionalSlackClientOAuthState) {
        //write validation methods
        //check if slackClientOAuthState is null or not
        if(optionalSlackClientOAuthState.isEmpty()){
            throw new InvalidSlackClientOAuthStateReceived("Invalid state key received from slack", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid state auth key received from slack");
        }
        SlackClientOAuthState slackClientOAuthState = optionalSlackClientOAuthState.get();
        if (slackClientOAuthState.getOrg() == null || slackClientOAuthState.getCreatedBy() == null) {
            log.error("invalid state value received");
            throw new InvalidSlackClientOAuthStateReceived("Invalid state key received from slack", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid state auth key received from slack");
        }
        //TODO: check for expiry and throw exception accordingly
    }


}
