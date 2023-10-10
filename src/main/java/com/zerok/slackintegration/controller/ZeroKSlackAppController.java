package com.zerok.slackintegration.controller;

import com.zerok.slackintegration.model.response.SlackIntegrationFetchResponse;
import com.zerok.slackintegration.service.ZeroKSlackIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slack/integration")
public class ZeroKSlackAppController {

    private final ZeroKSlackIntegrationService slackIntegrationService;

    @Autowired
    public ZeroKSlackAppController(ZeroKSlackIntegrationService slackIntegrationService) {
        this.slackIntegrationService = slackIntegrationService;
    }

    // get slack integration for user id and org id
    @GetMapping("/fetch")
    @ResponseBody
    public SlackIntegrationFetchResponse fetchSlackIntegration(@RequestParam String userId, @RequestParam String orgId) {
        return slackIntegrationService.fetchSlackIntegration(userId, orgId);
    }

    //disable slack integration :
    @PostMapping("/disable")
    @ResponseBody
    public void disableSlackIntegration(@RequestParam String userId, @RequestParam String orgId) {
        slackIntegrationService.disableSlackIntegration(userId, orgId);
    }

    //view slack integration

    //disconnect slack integration


}



