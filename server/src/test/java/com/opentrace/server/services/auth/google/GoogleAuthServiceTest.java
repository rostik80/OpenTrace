package com.opentrace.server.services.auth.google;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.services.auth.google.helpers.GoogleRemoteSource;
import com.opentrace.server.utils.builders.googleAuth.GoogleUrlBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleAuthServiceTest {

    @Mock
    private GoogleRemoteSource googleRemoteSource;

    @Mock
    private GoogleUrlBuilder urlBuilder;

    @InjectMocks
    private GoogleAuthService googleAuthService;

    @Test
    @DisplayName("Should return correct auth URL from builder using roles and publicKey")
    void shouldReturnAuthUrl() {
        String roles = "ADMIN";
        String publicKey = "test-public-key";
        String expectedUrl = "https://accounts.google.com/auth?state=encodedData";

        when(urlBuilder.buildFullAuthUrlWithAccountSelect(roles, publicKey)).thenReturn(expectedUrl);

        String resultUrl = googleAuthService.getAuthUrl(roles, publicKey);

        assertEquals(expectedUrl, resultUrl);
        verify(urlBuilder).buildFullAuthUrlWithAccountSelect(roles, publicKey);
    }

    @Test
    @DisplayName("Should fetch access token and then fetch user info")
    void shouldGetGoogleUser() {
        String authCode = "valid-auth-code";
        String mockAccessToken = "ya29.access-token-123";
        GoogleUserDTO expectedUser = new GoogleUserDTO();
        expectedUser.setEmail("user@gmail.com");
        expectedUser.setName("Google User");

        when(googleRemoteSource.fetchAccessToken(authCode)).thenReturn(mockAccessToken);
        when(googleRemoteSource.fetchUserInfo(mockAccessToken)).thenReturn(expectedUser);

        GoogleUserDTO resultUser = googleAuthService.getGoogleUser(authCode);

        assertEquals(expectedUser, resultUser);
        assertEquals("user@gmail.com", resultUser.getEmail());

        verify(googleRemoteSource).fetchAccessToken(authCode);
        verify(googleRemoteSource).fetchUserInfo(mockAccessToken);
    }
}