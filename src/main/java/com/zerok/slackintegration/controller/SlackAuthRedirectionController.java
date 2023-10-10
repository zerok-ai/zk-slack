package com.zerok.slackintegration.controller;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.service.SlackOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/slack/oauth")
public class SlackAuthRedirectionController {

    private final SlackOAuthService slackOAuthService;


    public SlackAuthRedirectionController(SlackOAuthService slackOAuthService) {
        this.slackOAuthService = slackOAuthService;
    }

    @PostMapping(value = "/initiate")
    public ResponseEntity<Void> redirect(@RequestParam("userId") String userId, @RequestParam("org") String org) {
        URI slackOAuthRedirectionUri = slackOAuthService.createSlackOAuthRedirectionUri(userId,org);
        System.out.println("redirection URL : "+ slackOAuthRedirectionUri.toString());
        return ResponseEntity.status(HttpStatus.FOUND).location(slackOAuthRedirectionUri).build();
    }

    @GetMapping("/redirect")
    public void slackAppOAuthRedirect(@RequestParam("code") String code,@RequestParam("state") String state) throws SlackApiException, IOException {
        System.out.println("code : "+code);
        slackOAuthService.fetchAndStoreClientAccessTokenFromTempToken(code,state);
    }
}
