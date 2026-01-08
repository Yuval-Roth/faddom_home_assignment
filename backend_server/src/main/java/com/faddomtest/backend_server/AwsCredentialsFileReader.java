package com.faddomtest.backend_server;

import com.faddomtest.backend_server.exceptions.AwsCredentialsFileReaderException;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AwsCredentialsFileReader implements AwsCredentialsProvider {
    private Map<String,String> credentialsMap;

    public void init(String pathToCredentials) throws AwsCredentialsFileReaderException {
        try {
            credentialsMap = new HashMap<>();
            try (BufferedReader varsFile = new BufferedReader(new FileReader(pathToCredentials))) {
                String line;
                while ((line = varsFile.readLine()) != null){
                    // expected format: key = value
                    if(line.contains("=")){
                        String[] keyValue = line.split("=");
                        if(keyValue.length != 2) {
                            throw new AwsCredentialsFileReaderException("Badly formatted line in credentials file: " + line);
                        }
                        String key = keyValue[0].strip();
                        String value = keyValue[1].strip();
                        credentialsMap.put(key,value);
                    }
                }
            }
            validateCredentials();
        } catch (IOException e) {
            if(e instanceof FileNotFoundException){
                throw new AwsCredentialsFileReaderException("""
                        Credentials file not found.
                        Make sure the credentials file exists in the following path:
                        %s""".formatted(pathToCredentials));
            }
            throw new RuntimeException(e);
        }
    }

    private void validateCredentials() throws AwsCredentialsFileReaderException {

        // Check if the credentials file is in the correct format
        if(getAccessKeyId() == null || getSecretAccessKey() == null){
            throw new AwsCredentialsFileReaderException("""
                    Invalid credentials file.
                    Please make sure the credentials file is in the following format:
                    aws_access_key_id = <your access key>
                    aws_secret_access_key = <your secret key>""");
        }

        // Test the credentials by making a call to AWS
        try (Ec2Client ec2Client = Ec2Client.builder()
                .credentialsProvider(this)
                .region(Region.US_EAST_1)
                .build()){

            ec2Client.describeInstances();
        } catch (Ec2Exception e) {
            throw new AwsCredentialsFileReaderException("""
                    Credentials were rejected by AWS.
                    Please make sure the credentials are up to date.""");
        }
    }

    private String getAccessKeyId(){
        return credentialsMap.get("aws_access_key_id");
    }

    private String getSecretAccessKey(){
        return credentialsMap.get("aws_secret_access_key");
    }

    @Override
    public AwsCredentials resolveCredentials() {
        if(credentialsMap == null || credentialsMap.isEmpty()){
            throw new RuntimeException("AwsCredentialsReader has not been initialized.");
        }
        return AwsBasicCredentials.create(
                getAccessKeyId(),
                getSecretAccessKey()
        );
    }

}
