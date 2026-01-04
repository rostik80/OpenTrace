package com.opentrace.server.utils.decoders;

import com.opentrace.server.properties.JwtProperties;
import com.opentrace.server.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtDecoderTest {

    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private JwtProvider jwtProvider;

    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        jwtDecoder = new JwtDecoder(jwtProperties, jwtProvider);
    }

    @Test
    @DisplayName("Should decode token using secret from properties when secret is valid")
    void shouldDecodeWithSecretFromProperties() {

        String validSecret = "this-is-a-very-secure-and-long-secret-key-32-chars";
        Key key = Keys.hmacShaKeyFor(validSecret.getBytes(StandardCharsets.UTF_8));

        when(jwtProperties.getSecret()).thenReturn(validSecret);

        String token = Jwts.builder()
                .setSubject("test-user")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Claims claims = jwtDecoder.decode(token);

        assertEquals("test-user", claims.getSubject());
        verify(jwtProvider, never()).getKey();
    }

    @Test
    @DisplayName("Should use dynamic key from JwtProvider when secret is null or too short")
    void shouldFallbackToJwtProvider() {

        Key dynamicKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        when(jwtProperties.getSecret()).thenReturn("short");
        when(jwtProvider.getKey()).thenReturn(dynamicKey);

        String token = Jwts.builder()
                .setSubject("fallback-user")
                .signWith(dynamicKey, SignatureAlgorithm.HS256)
                .compact();

        Claims claims = jwtDecoder.decode(token);

        assertEquals("fallback-user", claims.getSubject());
        verify(jwtProvider, atLeastOnce()).getKey();
    }

    @Test
    @DisplayName("Should throw exception for invalid token")
    void shouldThrowExceptionForInvalidToken() {

        String invalidToken = "invalid.token.here";
        when(jwtProperties.getSecret()).thenReturn("some-long-enough-secret-for-decoding-test");

        assertThrows(Exception.class, () -> jwtDecoder.decode(invalidToken));
    }
}