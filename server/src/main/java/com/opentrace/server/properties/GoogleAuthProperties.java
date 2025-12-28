package com.opentrace.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.auth")
@Data
public class GoogleAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;
}
