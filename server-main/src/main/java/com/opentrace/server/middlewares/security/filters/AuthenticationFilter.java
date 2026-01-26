package com.opentrace.server.middlewares.security.filters;

import com.opentrace.server.utils.decoders.JwtDecoder;
import com.opentrace.server.utils.mappers.JwtAuthenticationMapper;
import com.opentrace.server.services.auth.jwt.TokenService;
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
    private final TokenService tokenService;

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

            if (claims.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (isTokenVersionValid(claims)) {
                    var authToken = jwtAuthenticationMapper.toAuthentication(claims);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.err.println("JWT Version mismatch. Session revoked.");
                }
            }
        } catch (Exception e) {
            System.err.println("JWT Verification failed: " + e.getMessage());
        }
    }

    private boolean isTokenVersionValid(Claims claims) {
        String googleSub = claims.getSubject();

        Object versionInTokenObj = claims.get("version");

        if (versionInTokenObj == null) {
            return false;
        }

        Integer versionInToken = ((Number) versionInTokenObj).intValue();
        Integer currentVersionInDb = tokenService.getCurrentTokenVersionByGoogleSub(googleSub);

        return versionInToken.equals(currentVersionInDb);
    }
}