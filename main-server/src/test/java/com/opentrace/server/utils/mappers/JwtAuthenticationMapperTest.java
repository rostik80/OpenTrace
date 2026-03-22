package com.opentrace.server.utils.mappers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationMapperTest {

    private JwtAuthenticationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new JwtAuthenticationMapper();
    }

    @Test
    @DisplayName("Should correctly map claims to authentication object with roles")
    void shouldMapToAuthentication() {

        Claims claims = new DefaultClaims(Map.of(
                "sub", "google-user-123",
                "roles", List.of("USER", "ADMIN")
        ));

        UsernamePasswordAuthenticationToken auth = mapper.toAuthentication(claims);

        assertNotNull(auth);
        assertEquals("google-user-123", auth.getName());
        assertNull(auth.getCredentials());

        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        assertTrue(authorities.contains("ROLE_USER"));
        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertEquals(2, authorities.size());
    }

    @Test
    @DisplayName("Should handle empty or null roles gracefully")
    void shouldHandleNullRoles() {

        Claims claims = new DefaultClaims(Map.of("sub", "anonymous-user"));

        UsernamePasswordAuthenticationToken auth = mapper.toAuthentication(claims);

        assertNotNull(auth);
        assertTrue(auth.getAuthorities().isEmpty(), "Authorities should be empty if no roles provided");
    }
}