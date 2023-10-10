package com.zerok.slackintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/slack/events")
public class SlackAppController {

    //ALL slack events that happened on slack will come via this channel

    @PostMapping("/app-uninstalled")
    public ResponseEntity<Void> handleAppUninstalledEvent(@RequestBody Map<String, Object> payload) {
        // Handle the app_uninstalled event
        // Your logic to handle app uninstallation goes here

        return ResponseEntity.ok().build();
    }

}






