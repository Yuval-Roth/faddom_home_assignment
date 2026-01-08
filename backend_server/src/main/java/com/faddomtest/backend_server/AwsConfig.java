package com.faddomtest.backend_server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aws")
@Configuration
public class AwsConfig {
    private String credentialsPath;

    public String getCredentialsPath() {
        return credentialsPath;
    }
    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }
}
