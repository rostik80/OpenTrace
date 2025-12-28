package com.opentrace.server.services.auth.helpers;

import com.opentrace.server.properties.GoogleAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleUrlBuilder {

    private final GoogleAuthProperties props;
    private static final String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";


    public String buildDefaultAuthUrl() {
        return getBaseBuilder().toUriString();
    }

    public String buildFullAuthUrlWithAccountSelect() {
         return getBaseBuilder()
                .queryParam("access_type", "offline")
                .queryParam("prompt", "select_account")
                .build()
                .toUriString();
    }

    private UriComponentsBuilder getBaseBuilder() {
        return UriComponentsBuilder
                .fromUriString(GOOGLE_AUTH_BASE_URL)
                .queryParam("client_id", props.getClientId())
                .queryParam("redirect_uri", props.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile");
    }
}