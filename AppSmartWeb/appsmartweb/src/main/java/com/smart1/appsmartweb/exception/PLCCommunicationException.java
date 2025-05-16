package com.smart1.appsmartweb.exception;

public class PLCCommunicationException extends RuntimeException{
    public PLCCommunicationException(String message) {
        super(message);
    }

    public PLCCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
