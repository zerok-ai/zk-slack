package com.zerok.slackintegration.controller;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.service.SlackOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/v1/p/slack/oauth")
public class SlackAuthRedirectionController {

    private final SlackOAuthService slackOAuthService;

    @Autowired
    public SlackAuthRedirectionController(SlackOAuthService slackOAuthService) {
        this.slackOAuthService = slackOAuthService;
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> slackAppOAuthRedirect(@RequestParam("code") String code,@RequestParam("state") String state) throws SlackApiException, IOException {
        System.out.println("code : "+code);
        slackOAuthService.fetchAndStoreClientAccessTokenFromTempToken(code,state);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://dashboard.sandbox.zerok.dev/integrations/slack/list")).build();
    }
}
