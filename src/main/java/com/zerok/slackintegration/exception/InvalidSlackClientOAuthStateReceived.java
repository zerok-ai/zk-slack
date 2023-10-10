package com.zerok.slackintegration.exception;

public class InvalidSlackClientOAuthStateReceived extends BaseException{

    public InvalidSlackClientOAuthStateReceived(String errorMessage, int errorCode, String message) {
        super(errorMessage, errorCode, message);
    }

    public InvalidSlackClientOAuthStateReceived() {
    }
}
