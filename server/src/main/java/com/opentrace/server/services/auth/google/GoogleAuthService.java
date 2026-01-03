package com.opentrace.server.services.auth.google;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.services.auth.google.helpers.GoogleRemoteSource;
import com.opentrace.server.utils.builders.googleAuth.GoogleUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleRemoteSource googleRemoteSource;
    private final GoogleUrlBuilder urlBuilder;


    public String getAuthUrl(String roles) {
        return urlBuilder.buildFullAuthUrlWithAccountSelect(roles);
    }

    public GoogleUserDTO getGoogleUser(String authCode) {

        String accessToken = googleRemoteSource.fetchAccessToken(authCode);

        return googleRemoteSource.fetchUserInfo(accessToken);
    }
}