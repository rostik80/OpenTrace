package com.opentrace.server.utils.mappers;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationMapper {

    public UsernamePasswordAuthenticationToken toAuthentication(Claims claims) {
        String username = claims.getSubject();

        List<?> roles = claims.get("roles", List.class);

        List<SimpleGrantedAuthority> authorities = roles == null ? List.of() : roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}