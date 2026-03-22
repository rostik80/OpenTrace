package com.opentrace.server.services.auth.jwt;

import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.providers.security.JwtProvider;
import com.opentrace.server.services.RolePermissionService;
import com.opentrace.server.utils.parsers.RolePermissionParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenIssuanceServiceTest {

    @Mock
    private RolePermissionParser roleParser;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private TokenService tokenIssuanceService;

    @Test
    @DisplayName("Should issue token with intersection of requested and allowed roles")
    void shouldAuthorizeAndReturnTokenWithFilteredRoles() {
        UserDto userDTO = new UserDto();
        String rawRoles = "ADMIN, USER";
        List<String> parsedRequestedRoles = List.of("ADMIN", "USER");
        List<String> allowedRolesFromDb = List.of("USER");
        List<String> expectedRolesForIssuance = List.of("USER");
        String expectedToken = "mocked.jwt.token";

        when(roleParser.parseRoles(rawRoles)).thenReturn(parsedRequestedRoles);
        when(rolePermissionService.getAllowedRoles(userDTO)).thenReturn(allowedRolesFromDb);
        when(jwtProvider.createToken(userDTO, expectedRolesForIssuance)).thenReturn(expectedToken);

        String resultToken = tokenIssuanceService.authorize(userDTO, rawRoles);

        assertEquals(expectedToken, resultToken);
        verify(roleParser).parseRoles(rawRoles);
        verify(rolePermissionService).getAllowedRoles(userDTO);
        verify(jwtProvider).createToken(userDTO, expectedRolesForIssuance);
    }

    @Test
    @DisplayName("Should issue token with REQUESTER role when no requested roles are allowed")
    void shouldFallbackToRequesterRole() {
        UserDto userDTO = new UserDto();
        String rawRoles = "ADMIN";
        List<String> parsedRequestedRoles = List.of("ADMIN");
        List<String> allowedRolesFromDb = List.of("WORKER");
        List<String> fallbackRoles = List.of("REQUESTER");
        String expectedToken = "fallback.jwt.token";

        when(roleParser.parseRoles(rawRoles)).thenReturn(parsedRequestedRoles);
        when(rolePermissionService.getAllowedRoles(userDTO)).thenReturn(allowedRolesFromDb);
        when(jwtProvider.createToken(userDTO, fallbackRoles)).thenReturn(expectedToken);

        String resultToken = tokenIssuanceService.authorize(userDTO, rawRoles);

        assertEquals(expectedToken, resultToken);
        verify(jwtProvider).createToken(userDTO, fallbackRoles);
    }
}