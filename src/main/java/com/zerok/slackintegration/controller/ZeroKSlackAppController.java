package com.zerok.slackintegration.controller;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.response.DashboardResponse;
import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.model.response.SlackIntegrationInitiateResponse;
import com.zerok.slackintegration.service.SlackOAuthService;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    public DashboardResponse redirect(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true)  String org) {
        return slackOAuthService.createSlackOAuthRedirectionUri(userId,org);
    }

    @GetMapping(value = "/initiate")
    public DashboardResponse testredirect(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true)  String org) {
        return slackOAuthService.createSlackOAuthRedirectionUri(userId,org);
    }

    // get slack integration for user id and org id
    @GetMapping("/fetch")
    @ResponseBody
    public DashboardResponse fetchSlackIntegration(@RequestHeader(value = "X-USER-ID", required = true) String userId, @RequestHeader(value = "X-ORG-ID", required = true) String orgId) {
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



