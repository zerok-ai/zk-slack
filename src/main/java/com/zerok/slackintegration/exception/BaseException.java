package com.zerok.slackintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private String errorMessage;
    private int errorCode;
    private String message;

    private BaseException(String message, int code){
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
        this.message = message;
    }
}
