package com.opentrace.server.controllers.api.v1.auth;

import com.opentrace.server.services.auth.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final GoogleAuthService googleAuthService;

    @GetMapping("/google")
    public void redirectToGoogle(
            @RequestParam(value = "roles", defaultValue = "REQUESTER") String roles,
            HttpServletResponse response
    ) throws IOException {

        String url = googleAuthService.getAuthUrl(roles);
        response.sendRedirect(url);
    }



    @GetMapping("/google/callback")
    public ResponseEntity<?> handleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String roles,
            @RequestParam(value = "error", required = false) String error
    ) {
        if (error != null) {
            return ResponseEntity.badRequest().body("Google error: " + error);
        }

        if (code == null) {
            return ResponseEntity.badRequest().body("Missing code from Google");
        }

        String jwt = googleAuthService.processGoogleCallback(code, roles);

        return ResponseEntity.ok(jwt);
    }
}