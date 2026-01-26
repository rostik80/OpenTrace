package com.opentrace.server.middlewares.security.filters;

import com.opentrace.server.utils.decoders.JwtDecoder;
import com.opentrace.server.utils.mappers.JwtAuthenticationMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private JwtAuthenticationMapper jwtAuthenticationMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Claims claims;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should authenticate when valid Bearer token is provided")
    void shouldAuthenticateWithValidToken() throws Exception {
        String token = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("user@example.com");

        var auth = new UsernamePasswordAuthenticationToken("user", null, null);
        when(jwtAuthenticationMapper.toAuthentication(claims)).thenReturn(auth);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(auth, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Should skip authentication when Authorization header is missing")
    void shouldSkipWhenNoHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtDecoder);
    }

    @Test
    @DisplayName("Should skip authentication when header does not start with Bearer")
    void shouldSkipWhenInvalidHeaderFormat() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        authenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtDecoder);
    }

    @Test
    @DisplayName("Should not crash and continue chain if JWT decoding fails")
    void shouldContinueChainOnException() throws Exception {
        String token = "invalid.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtDecoder.decode(token)).thenThrow(new RuntimeException("Invalid signature"));

        authenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}