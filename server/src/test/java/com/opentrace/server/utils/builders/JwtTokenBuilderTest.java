package com.opentrace.server.utils.builders;

import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenBuilderTest {

    @Mock
    private JwtProperties jwtProperties;

    private JwtTokenBuilder jwtTokenBuilder;
    private Key testKey;

    @BeforeEach
    void setUp() {
        jwtTokenBuilder = new JwtTokenBuilder(jwtProperties);
        testKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    @Test
    @DisplayName("Should build a valid JWT with correct claims")
    void shouldBuildValidJwtWithCorrectClaims() {

        UserEntity user = new UserEntity();
        user.setGoogleSub("google-123");
        user.setTokenVersion(1);
        user.setPriority(5);

        List<String> roles = List.of("USER", "WORKER");
        long expirationMs = 3600000;

        when(jwtProperties.getExpiration()).thenReturn(expirationMs);

        String token = jwtTokenBuilder.buildToken(user, roles, testKey);

        assertNotNull(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(testKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("google-123", claims.getSubject());
        assertEquals(roles, claims.get("roles", List.class));
        assertEquals(1, claims.get("version", Integer.class));
        assertEquals(5, claims.get("priority", Integer.class));

        assertTrue(claims.getExpiration().after(new java.util.Date()));
    }
}