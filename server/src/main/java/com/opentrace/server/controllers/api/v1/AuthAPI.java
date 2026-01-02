package com.opentrace.server.controllers.api.v1;

import com.opentrace.server.models.api.response.ApiResponse;
import com.opentrace.server.services.auth.AuthService;
import com.opentrace.server.services.auth.google.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPI {

    private final GoogleAuthService googleAuthService;
    private final AuthService authService;

    @GetMapping("/google")
    public void redirectToGoogle(
            @RequestParam(value = "roles", defaultValue = "REQUESTER") String roles,
            HttpServletResponse response
    ) throws IOException {

        String url = googleAuthService.getAuthUrl(roles);
        response.sendRedirect(url);
    }



    @GetMapping("/google/callback")
    public ResponseEntity<ApiResponse<String>> handleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String roles,
            @RequestParam(value = "error", required = false) String error
    ) {
        if (error != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "Google error: " + error));
        }

        if (code == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(400, "Missing code from Google"));
        }

        String jwt = authService.authorize(code, roles);

        return ResponseEntity.ok(ApiResponse.ok(jwt));
    }
}