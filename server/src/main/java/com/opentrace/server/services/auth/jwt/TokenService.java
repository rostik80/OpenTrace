package com.opentrace.server.services.auth.jwt;

import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.services.RolePermissionService;
import com.opentrace.server.services.UserService;
import com.opentrace.server.utils.parsers.RolePermissionParser;
import com.opentrace.server.providers.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final RolePermissionParser roleParser;
    private final JwtProvider jwtProvider;
    private final RolePermissionService rolePermissionService;
    private final UserService userService;

    public String authorize(UserDto userDTO, String requestedRoles) {
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

    public void revokeAllSessionsByGoogleSub(String googleSub) {
        UserDto userDto = userService.getByGoogleSub(googleSub);
        userDto.setTokenVersion(userDto.getTokenVersion() + 1);
        userService.updateByGoogleSub(googleSub,userDto);
    }

    public Integer getCurrentTokenVersionByGoogleSub(String googleSub) {
        return userService.getFieldByGoogleSub(googleSub,
                        (dto, entity) -> dto.setTokenVersion(entity.getTokenVersion()))
                .getTokenVersion();
    }
}