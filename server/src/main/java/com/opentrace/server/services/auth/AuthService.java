package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.services.UserService;
import com.opentrace.server.services.auth.jwt.TokenIssuanceService;
import com.opentrace.server.services.auth.google.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;
    private final TokenIssuanceService tokenIssuanceService;

    public String googleAuthorize(String authCode, String roles) {

        GoogleUserDTO googleUser = googleAuthService.getGoogleUser(authCode);

        UserDTO user = userService.saveOrUpdate(googleUser);

        return tokenIssuanceService.authorize(user, roles);
    }
}

