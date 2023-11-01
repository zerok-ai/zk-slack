package com.zerok.slackintegration.exception;

public class SlackInferencePublishException extends BaseException{

    public SlackInferencePublishException(String errorMessage, int errorCode, String message) {
        super(errorMessage, errorCode, message);
    }

    public SlackInferencePublishException() {
    }
}
