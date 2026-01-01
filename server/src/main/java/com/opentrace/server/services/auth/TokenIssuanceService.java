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


    public String authorize(UserDTO userDto, String requestedRoles) {

        System.out.println(requestedRoles);
        List<String> rolesForIssuance = roleParser.parseRoles(requestedRoles);
        System.out.println(rolesForIssuance);

        roleService.assignRoles(userDto, rolesForIssuance);

        return jwtProvider.createToken(
                userMapper.toEntity(userDto),
                rolesForIssuance
        );
    }
}