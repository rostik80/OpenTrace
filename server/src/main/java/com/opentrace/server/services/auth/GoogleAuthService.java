package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleTokenRequestDTO;
import com.opentrace.server.services.auth.helpers.GoogleTokenBuilder;
import com.opentrace.server.services.auth.helpers.GoogleUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleUrlBuilder urlBuilder;
    private final GoogleTokenBuilder tokenBuilder;
    private final RestTemplate restTemplate;

    private static final String ACCESS_TOKEN_KEY = "access_token";

    public String getAuthUrl() {
        return urlBuilder.buildFullAuthUrlWithAccountSelect();
    }

    public Map<String, Object> processGoogleCallback(String code) {
        GoogleTokenRequestDTO tokenRequest = tokenBuilder.buildTokenRequest(code);

        Map<String, Object> tokenResponse = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token",
                tokenRequest,
                Map.class
        );

        String accessToken = (String) tokenResponse.get("access_token");

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        return restTemplate.getForObject(userInfoUrl, Map.class);
    }
}