package com.opentrace.server.controllers.api.v1.auth;

import com.opentrace.server.services.auth.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final GoogleAuthService googleAuthService;

    // entry point
    @GetMapping("/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = googleAuthService.getAuthUrl();
        response.sendRedirect(url);
    }

    // Callback
    @GetMapping("/google/callback")
    public ResponseEntity<?> handleCallback(@RequestParam(value = "code", required = false) String code,
                                            @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            return ResponseEntity.badRequest().body("Google error: " + error);
        }

        if (code == null) {
            return ResponseEntity.badRequest().body("Missing code from Google");
        }

        Map<String, Object> userData = googleAuthService.processGoogleCallback(code);

        return ResponseEntity.ok(userData);
    }
}