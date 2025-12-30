package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.services.UserService;
import com.opentrace.server.services.auth.helpers.GoogleRemoteSource;
import com.opentrace.server.builders.auth.GoogleUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleRemoteSource googleRemoteSource;
    private final GoogleUrlBuilder urlBuilder;
    private final TokenIssuanceService tokenIssuanceService;

    private final UserService userService;


    public String getAuthUrl(String roles) {
        return urlBuilder.buildFullAuthUrlWithAccountSelect(roles);
    }

    public String processGoogleCallback(String authCode, String roles) {

        String accessToken = googleRemoteSource.fetchAccessToken(authCode);

        GoogleUserDTO googleUser = googleRemoteSource.fetchUserInfo(accessToken);

        UserDTO user = userService.saveOrUpdate(googleUser);

        return tokenIssuanceService.authorizeAndIssueToken(user, roles);
    }
}