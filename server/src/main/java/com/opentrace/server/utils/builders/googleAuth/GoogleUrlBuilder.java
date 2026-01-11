package com.opentrace.server.utils.builders.googleAuth;

import com.opentrace.server.properties.GoogleAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;


@Component
@RequiredArgsConstructor
public class GoogleUrlBuilder {

    private final GoogleAuthProperties props;
    private static final String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    public String buildFullAuthUrlWithAccountSelect(String roles, String publicKey) {

        String state = encodeState(roles, publicKey);

        return getBaseBuilder()
                .queryParam("access_type", "offline")
                .queryParam("prompt", "select_account")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    private String encodeState(String roles, String publicKey) {
        String combined = roles + "|" + publicKey;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(combined.getBytes());
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