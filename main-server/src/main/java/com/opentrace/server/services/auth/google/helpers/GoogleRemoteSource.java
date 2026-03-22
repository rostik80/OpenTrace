package com.opentrace.server.services.auth.google.helpers;

import com.opentrace.server.models.dto.GoogleTokenRequestDto;
import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.utils.builders.googleAuth.GoogleTokenBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class GoogleRemoteSource {

    private final RestTemplate restTemplate;
    private final GoogleTokenBuilder tokenBuilder;

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=";
    private static final String ACCESS_TOKEN_KEY = "access_token";


    public String fetchAccessToken(String authCode) {

        GoogleTokenRequestDto tokenBodyRequest = tokenBuilder.buildTokenBodyRequest(authCode);

        Map<String, Object> response = restTemplate.postForObject(TOKEN_URL, tokenBodyRequest, Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Google API did not return an access token");
        }
        return (String) response.get(ACCESS_TOKEN_KEY);
    }

    public GoogleUserDto fetchUserInfo(String accessToken) {

        return restTemplate.getForObject(USER_INFO_URL + accessToken, GoogleUserDto.class);
    }
}
