package com.opentrace.server.utils.decoders;

import com.opentrace.server.properties.JwtProperties;
import com.opentrace.server.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtDecoder {

    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getEffectiveKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getEffectiveKey() {
        String secret = jwtProperties.getSecret();

        if (secret != null && secret.getBytes(StandardCharsets.UTF_8).length >= 32) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }

        return jwtProvider.getKey();
    }
}