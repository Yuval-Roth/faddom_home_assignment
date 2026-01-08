package com.faddomtest.backend_server;

import com.faddomtest.backend_server.exceptions.AwsCredentialsFileReaderException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.time.LocalDateTime;

@Service
public class AwsService {

    private Ec2Client ec2;

    public AwsService(AwsConfig awsConfig) {
        // setup credentials provider
        AwsCredentialsFileReader credentialsProvider = new AwsCredentialsFileReader();
        try {
            credentialsProvider.init(awsConfig.getCredentialsPath());
        } catch (AwsCredentialsFileReaderException e) {
            throw new RuntimeException(e);
        }
        if(! credentialsProvider.validateCredentials()){
            throw new RuntimeException("AWS credentials were rejected");
        }
        ec2 = Ec2Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    }


    //TODO: change return type and return actual data
    public void getCpuUsageStatistics(String instanceIp, LocalDateTime startTime, LocalDateTime endTime) {

    }
}
