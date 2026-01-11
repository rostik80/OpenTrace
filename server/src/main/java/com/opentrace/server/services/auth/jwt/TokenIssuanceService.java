package com.opentrace.server.services.auth.jwt;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.services.RolePermissionService;
import com.opentrace.server.utils.parsers.RolePermissionParser;
import com.opentrace.server.providers.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TokenIssuanceService {

    private final RolePermissionParser roleParser;
    private final JwtProvider jwtProvider;
    private final RolePermissionService rolePermissionService;

    public String authorize(UserDTO userDTO, String requestedRoles) {
        List<String> parsedRequestedRoles = roleParser.parseRoles(requestedRoles);
        List<String> allowedRolesFromDb = rolePermissionService.getAllowedRoles(userDTO);

        List<String> rolesForIssuance = new ArrayList<>(parsedRequestedRoles.stream()
                .filter(allowedRolesFromDb::contains)
                .toList());

        if (rolesForIssuance.isEmpty()) {
            rolesForIssuance.add("REQUESTER");
        }

        return jwtProvider.createToken(
                userDTO,
                rolesForIssuance
        );
    }
}