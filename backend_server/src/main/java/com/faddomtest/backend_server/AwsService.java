package com.faddomtest.backend_server;

import com.faddomtest.backend_server.exceptions.AwsCredentialsFileReaderException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Service
public class AwsService {

    private AwsCredentialsProvider credentialsProvider;

    public AwsService(AppConfig appConfig) {

        // setup credentials provider
        AwsCredentialsFileReader credentialsReader = new AwsCredentialsFileReader();
        try {
            credentialsReader.init(appConfig.getCredentialsPath());
            credentialsProvider = credentialsReader;
        } catch (AwsCredentialsFileReaderException e) {
            throw new RuntimeException(e);
        }
    }


}
