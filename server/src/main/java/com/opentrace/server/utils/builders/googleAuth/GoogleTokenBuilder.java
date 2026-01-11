package com.opentrace.server.utils.builders.googleAuth;

import com.opentrace.server.models.dto.GoogleTokenRequestDto;
import com.opentrace.server.properties.GoogleAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GoogleTokenBuilder {

    private final GoogleAuthProperties props;

    public GoogleTokenRequestDto buildTokenBodyRequest(String code) {
        return GoogleTokenRequestDto.builder()
                .code(code)
                .client_id(props.getClientId())
                .client_secret(props.getClientSecret())
                .redirect_uri(props.getRedirectUri())
                .grant_type("authorization_code")
                .build();
    }
}
