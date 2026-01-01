package com.opentrace.server.parsers;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleParser {

    public List<String> parseRoles(String roles) {
        if (roles == null || roles.isBlank()) {
            return List.of("REQUESTER");
        }
        System.out.println("RoleParser");
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }
}