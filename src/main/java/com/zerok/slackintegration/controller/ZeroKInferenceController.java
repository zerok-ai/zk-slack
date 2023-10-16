package com.zerok.slackintegration.controller;


import com.slack.api.methods.SlackApiException;
import com.zerok.slackintegration.model.ZeroKIssueInference;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/i/slack/publish")
public class ZeroKInferenceController {

    private final ZeroKSlackIntegrationService slackIntegrationService;

    @Autowired
    public ZeroKInferenceController(ZeroKSlackIntegrationService slackIntegrationService) {
        this.slackIntegrationService = slackIntegrationService;
    }

    @PostMapping("/inference")
    public List<String> retrieveCoursesForStudent(@RequestBody  ZeroKIssueInference zeroKIssueInference) throws SlackApiException, IOException {
        return slackIntegrationService.publishInferenceToSlack(zeroKIssueInference);
    }

}
