package com.zerok.slackintegration.exception;

public class SlackIntegrationInitiateException extends BaseException{

    public SlackIntegrationInitiateException(String errorMessage, int errorCode, String message) {
        super(errorMessage, errorCode, message);
    }

    public SlackIntegrationInitiateException() {
    }
}
