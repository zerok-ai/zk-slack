package com.zerok.slackintegration.controller;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.service.SlackOAuthService;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/v1/u/slack/integration")
public class ZeroKSlackAppController {

    private final ZeroKSlackIntegrationService slackIntegrationService;
    private final SlackOAuthService slackOAuthService;

    @Autowired
    public ZeroKSlackAppController(ZeroKSlackIntegrationService slackIntegrationService, SlackOAuthService slackOAuthService) {
        this.slackIntegrationService = slackIntegrationService;
        this.slackOAuthService = slackOAuthService;
    }

    @PostMapping(value = "/initiate")
    public ResponseEntity<Void> redirect(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true)  String org) {
        URI slackOAuthRedirectionUri = slackOAuthService.createSlackOAuthRedirectionUri(userId,org);
        System.out.println("redirection URL : "+ slackOAuthRedirectionUri.toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(slackOAuthRedirectionUri).build();
    }

    @GetMapping(value = "/initiate")
    public ResponseEntity<Void> testredirect(@RequestParam(name = "userId") String userId, @RequestParam(name = "orgId") String org) {
        URI slackOAuthRedirectionUri = slackOAuthService.createSlackOAuthRedirectionUri(userId,org);
        System.out.println("redirection URL : "+ slackOAuthRedirectionUri.toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(slackOAuthRedirectionUri).build();
    }

    // get slack integration for user id and org id
    @GetMapping("/fetch")
    @ResponseBody
    public SlackIntegrationFetchResponse fetchSlackIntegration(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true) String orgId) {
        return slackIntegrationService.fetchSlackIntegration(userId, orgId);
    }

    //disable slack integration :
    @PostMapping("/disable")
    @ResponseBody
    public ResponseEntity<String> disableSlackIntegration(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true) String orgId) throws SlackApiException, IOException {
        slackIntegrationService.disableSlackIntegration(userId, orgId);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}



