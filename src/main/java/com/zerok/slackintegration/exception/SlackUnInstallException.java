package com.zerok.slackintegration.exception;

public class SlackUnInstallException extends BaseException{
    public SlackUnInstallException(String errorMessage, int errorCode, String message) {
        super(errorMessage, errorCode, message);
    }
}
