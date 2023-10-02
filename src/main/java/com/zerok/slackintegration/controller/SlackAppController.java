package com.zerok.slackintegration.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;


@WebServlet("/slack/events")
public class SlackAppController extends SlackAppServlet {

    public SlackAppController(App app) {
        super(app);
    }
}






