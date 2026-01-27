package com.opentrace.server.utils.builders;

import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtTokenBuilder {

    private final JwtProperties jwtProperties;

    public String buildToken(UserEntity user, List<String> roles, Key key) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .setSubject(user.getGoogleSub())
                .claim("roles", roles)
                .claim("version", user.getTokenVersion())
                .claim("priority", user.getPriority())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}