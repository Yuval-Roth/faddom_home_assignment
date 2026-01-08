package com.faddomtest.backend_server.exceptions;

public class AwsCredentialsFileReaderException extends Exception {
    public AwsCredentialsFileReaderException(String message) {
        super(message, null, false, false);
    }
}
