package com.opentrace.server.security;

import com.opentrace.server.builders.JwtTokenBuilder;
import com.opentrace.server.mappers.UserMapper;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.properties.JwtProperties;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final JwtTokenBuilder tokenBuilder;
    private Key key;

    public String createToken(UserDTO user, List<String> roles) {
        return tokenBuilder.buildToken(userMapper.toEntity(user), roles, key);
    }

    @Override
    public void afterPropertiesSet() {
        String secret = jwtProperties.getSecret();

        if (secret == null || secret.isBlank()) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("WARNING: JWT Secret not found in properties!");
            System.err.println("A temporary key has been generated for this session.");
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } else {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }
}