package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.services.RolePermissionService;
import com.opentrace.server.services.UserService;
import com.opentrace.server.services.auth.jwt.TokenService;
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
    private TokenService tokenIssuanceService;
    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should successfully complete the full Google authorization flow with RSA key and role assignment")
    void shouldCompleteGoogleAuthorization() {
        String code = "auth-code";
        String requestedRoles = "USER";
        String publicKey = "test-public-key";
        String finalToken = "jwt-token";

        GoogleUserDto googleUser = new GoogleUserDto();
        UserDto appUser = new UserDto();

        when(googleAuthService.getGoogleUser(code)).thenReturn(googleUser);
        when(userService.save(googleUser, publicKey)).thenReturn(appUser);
        when(tokenIssuanceService.authorize(appUser, requestedRoles)).thenReturn(finalToken);

        String result = authService.authorize(code, requestedRoles, publicKey);

        assertEquals(finalToken, result);

        verify(googleAuthService).getGoogleUser(code);
        verify(userService).save(googleUser, publicKey);
        verify(rolePermissionService).assignRolesPermission(appUser);
        verify(tokenIssuanceService).authorize(appUser, requestedRoles);
    }
}