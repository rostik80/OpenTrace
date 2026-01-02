package com.opentrace.server.builders.googleAuth;

import com.opentrace.server.models.dto.GoogleTokenRequestDTO;
import com.opentrace.server.properties.GoogleAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GoogleTokenBuilder {

    private final GoogleAuthProperties props;

    public GoogleTokenRequestDTO buildTokenBodyRequest(String code) {
        return GoogleTokenRequestDTO.builder()
                .code(code)
                .client_id(props.getClientId())
                .client_secret(props.getClientSecret())
                .redirect_uri(props.getRedirectUri())
                .grant_type("authorization_code")
                .build();
    }
}
