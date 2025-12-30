package com.opentrace.server.services.auth;

import com.opentrace.server.mappers.UserMapper;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.parsers.RoleParser;
import com.opentrace.server.services.RoleService;
import com.opentrace.server.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TokenIssuanceService {

    private final RoleParser roleParser;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;


    public String authorizeAndIssueToken(UserDTO userDto, String roles) {

        List<String> requestedRoles = roleParser.parseRoles(roles);

        List<String> allowedRoles = roleService.getUserRoles(userDto.getId());
        validatePermissions(requestedRoles, allowedRoles);

        return jwtProvider.createToken(
                userMapper.toEntity(userDto),
                requestedRoles
        );
    }

    private void validatePermissions(List<String> requested, List<String> allowed) {
        boolean hasAll = allowed.containsAll(requested);

        if (!hasAll) {
            throw new RuntimeException("Access denied: you do not have permission for one of the selected roles.");
        }
    }
}