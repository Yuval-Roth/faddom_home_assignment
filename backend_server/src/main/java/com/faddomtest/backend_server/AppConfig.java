package com.faddomtest.backend_server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "app")
@Configuration
public class AppConfig {
    private String credentialsPath;
    private String instanceIp;

    public String getInstanceIp() {
        return instanceIp;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }
}
