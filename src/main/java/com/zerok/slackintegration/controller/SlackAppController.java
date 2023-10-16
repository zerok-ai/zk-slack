package com.zerok.slackintegration.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/p/slack")
public class SlackAppController {

    //ALL slack events that happened on slack will come via this channel

    @PostMapping("/events/app-uninstalled")
    public ResponseEntity<Void> handleAppUninstalledEvent(@RequestBody Map<String, Object> payload) {
        // Handle the app_uninstalled event
        // Your logic to handle app uninstallation goes here

        return ResponseEntity.ok().build();
    }

    @PostMapping("/events")
    public Object handleAppUninstalledEventTemp(@RequestBody Map<String, Object> payload) {
        // Handle the app_uninstalled event
        // Your logic to handle app uninstallation goes here
        
        return payload.get("challenge");
    }



}






