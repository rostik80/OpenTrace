package com.opentrace.server.services.auth.google.helpers;

import com.opentrace.server.models.dto.GoogleTokenRequestDTO;
import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.utils.builders.googleAuth.GoogleTokenBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleRemoteSourceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GoogleTokenBuilder tokenBuilder;

    @InjectMocks
    private GoogleRemoteSource googleRemoteSource;

    @Test
    @DisplayName("Should successfully fetch access token when Google returns valid response")
    void shouldFetchAccessTokenSuccessfully() {

        String code = "valid-code";
        GoogleTokenRequestDTO requestBody = new GoogleTokenRequestDTO();
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("access_token", "ya29.test-token");

        when(tokenBuilder.buildTokenBodyRequest(code)).thenReturn(requestBody);
        when(restTemplate.postForObject(eq("https://oauth2.googleapis.com/token"), eq(requestBody), eq(Map.class)))
                .thenReturn(mockResponse);

        String token = googleRemoteSource.fetchAccessToken(code);

        assertEquals("ya29.test-token", token);
    }

    @Test
    @DisplayName("Should throw exception when Google response is empty or missing token")
    void shouldThrowExceptionWhenTokenMissing() {

        String code = "invalid-code";
        when(tokenBuilder.buildTokenBodyRequest(code)).thenReturn(new GoogleTokenRequestDTO());

        Map<String, Object> emptyResponse = new HashMap<>();
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class)))
                .thenReturn(emptyResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                googleRemoteSource.fetchAccessToken(code)
        );

        assertEquals("Google API did not return an access token", exception.getMessage());
    }

    @Test
    @DisplayName("Should fetch user info using access token")
    void shouldFetchUserInfo() {

        String token = "some-token";
        GoogleUserDTO expectedUser = new GoogleUserDTO();
        expectedUser.setEmail("test@gmail.com");

        String expectedUrl = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token;
        when(restTemplate.getForObject(expectedUrl, GoogleUserDTO.class)).thenReturn(expectedUser);

        GoogleUserDTO result = googleRemoteSource.fetchUserInfo(token);

        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
    }
}