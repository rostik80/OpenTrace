package com.opentrace.server.utils.parsers;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RolePermissionParser {

    public List<String> parseRoles(String roles) {
        if (roles == null || roles.isBlank()) {
            return List.of("REQUESTER");
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}