package com.opentrace.server.controllers.api.v1;

import com.opentrace.server.models.api.response.ApiResponseModel;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseModel<UserDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String googleSub = authentication.getName();

        UserDTO user = userService.getByGoogleSub(googleSub);

        return ResponseEntity.ok(ApiResponseModel.ok(user));
    }
}