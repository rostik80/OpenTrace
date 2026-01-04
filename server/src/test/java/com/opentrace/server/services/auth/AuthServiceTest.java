package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.services.UserService;
import com.opentrace.server.services.auth.jwt.TokenIssuanceService;
import com.opentrace.server.services.auth.google.GoogleAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GoogleAuthService googleAuthService;
    @Mock
    private UserService userService;
    @Mock
    private TokenIssuanceService tokenIssuanceService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should successfully complete the full Google authorization flow")
    void shouldCompleteGoogleAuthorization() {

        String code = "auth-code";
        String requestedRoles = "USER";
        String finalToken = "jwt-token";

        GoogleUserDTO googleUser = new GoogleUserDTO();
        UserDTO appUser = new UserDTO();

        when(googleAuthService.getGoogleUser(code)).thenReturn(googleUser);
        when(userService.saveOrUpdate(googleUser)).thenReturn(appUser);
        when(tokenIssuanceService.authorize(appUser, requestedRoles)).thenReturn(finalToken);

        String result = authService.googleAuthorize(code, requestedRoles);

        assertEquals(finalToken, result);

        verify(googleAuthService).getGoogleUser(code);
        verify(userService).saveOrUpdate(googleUser);
        verify(tokenIssuanceService).authorize(appUser, requestedRoles);
    }
}