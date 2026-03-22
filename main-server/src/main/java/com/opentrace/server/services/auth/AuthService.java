package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.services.RolePermissionService;
import com.opentrace.server.services.UserService;
import com.opentrace.server.services.auth.jwt.TokenService;
import com.opentrace.server.services.auth.google.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final TokenService tokenIssuanceService;
    private final RolePermissionService rolePermissionService;

    public String authorize(String authCode, String roles, String publicKey) {

        GoogleUserDto googleUser = googleAuthService.getGoogleUser(authCode);

        UserDto user = userService.save(googleUser, publicKey);
        rolePermissionService.assignRolesPermission(user);

        return tokenIssuanceService.authorize(user, roles);
    }
}

