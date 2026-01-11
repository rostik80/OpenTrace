package com.opentrace.server.controllers.api.v1;

import com.opentrace.server.models.api.response.ApiResponseModel;
import com.opentrace.server.services.auth.AuthService;
import com.opentrace.server.services.auth.google.GoogleAuthService;
import com.opentrace.server.utils.codecs.GoogleStatePacker;
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
    private final GoogleStatePacker googleBaseCodec;

    @GetMapping("/google")
    public void redirectToGoogle(
            @RequestParam(value = "roles", defaultValue = "REQUESTER") String roles,
            @RequestParam String publicKey,
            HttpServletResponse response
    ) throws IOException {

        String url = googleAuthService.getAuthUrl(roles, publicKey);
        response.sendRedirect(url);
    }



    @GetMapping("/google/callback")
    public ResponseEntity<ApiResponseModel<String>> handleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error
    ) {

        if (error != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseModel.error(400, "Google error: " + error));
        }

        if (code == null || state == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseModel.error(400, "Missing code or state from Google"));
        }

        GoogleStatePacker.StateData data = googleBaseCodec.decode(state);

        if (data.publicKey() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseModel.error(400, "Public key is missing in state"));
        }

        String jwt = authService.authorize(code, data.roles(), data.publicKey());

        return ResponseEntity.ok(ApiResponseModel.ok(jwt));
    }

    // <-- revoke All tokens
}