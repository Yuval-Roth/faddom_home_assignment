package com.faddomtest.backend_server.business;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aws")
@Configuration
public class AwsConfig {

    // required param
    private String credentialsPath;
    public String getCredentialsPath() { return credentialsPath; }
    public void setCredentialsPath(String value) { credentialsPath = value; }

    // optional param
    private boolean validateCredentials = false;
    public boolean getValidateCredentials(){ return validateCredentials; }
    public void setValidateCredentials(boolean value) { validateCredentials = value; }
}
