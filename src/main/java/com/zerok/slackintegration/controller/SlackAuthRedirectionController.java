package com.zerok.slackintegration.controller;

import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.service.SlackOAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/p/slack/oauth")
public class SlackAuthRedirectionController {

    private final SlackOAuthService slackOAuthService;

    public SlackAuthRedirectionController(SlackOAuthService slackOAuthService) {
        this.slackOAuthService = slackOAuthService;
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> slackAppOAuthRedirect(@RequestParam("code") String code,@RequestParam("state") String state) throws SlackApiException, IOException {
        System.out.println("code : "+code);
        slackOAuthService.fetchAndStoreClientAccessTokenFromTempToken(code,state);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://dashboard.loadcloud01.getanton.com/integrations/slack/list")).build();
    }
}
