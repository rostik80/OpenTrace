package com.opentrace.server.middlewares.security.filters;

import com.opentrace.server.utils.decoders.JwtDecoder;
import com.opentrace.server.utils.mappers.JwtAuthenticationMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationMapper jwtAuthenticationMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        authenticateRequest(request);

        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) { return; }

        try {
            String token = header.substring(7);
            Claims claims = jwtDecoder.decode(token);

            System.out.println(token);

            if (claims.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var authToken = jwtAuthenticationMapper.toAuthentication(claims);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            System.err.println("JWT Verification failed: " + e.getMessage());
        }
    }
}