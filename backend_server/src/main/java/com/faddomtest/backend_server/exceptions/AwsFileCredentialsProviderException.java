package com.faddomtest.backend_server.exceptions;

public class AwsFileCredentialsProviderException extends Exception {
    public AwsFileCredentialsProviderException(String message) {
        super(message, null, false, false);
    }
}
