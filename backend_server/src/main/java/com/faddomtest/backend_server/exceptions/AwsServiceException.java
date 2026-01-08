package com.faddomtest.backend_server.exceptions;

public class AwsServiceException extends Exception {
    public AwsServiceException(String message) {
        super(message, null, false, false);
    }
}
