package com.opentrace.server.services.auth;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.parsers.RolePermissionParser;
import com.opentrace.server.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenIssuanceService {

    private final RolePermissionParser roleParser;

    private final JwtProvider jwtProvider;


    public String authorize(UserDTO userDTO, String requestedRoles) {

        List<String> rolesForIssuance = roleParser.parseRoles(requestedRoles);

        return jwtProvider.createToken(
                userDTO,
                rolesForIssuance
        );
    }
}