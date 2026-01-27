package com.opentrace.server.controllers.api.v1;

import com.opentrace.shared.models.api.response.ApiResponseModel;
import com.opentrace.server.models.dto.UserDto;
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
public class UserApi {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseModel<UserDto>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String googleSub = authentication.getName(); // <-- move to the controller

        UserDto user = userService.getByGoogleSub(googleSub);

        return ResponseEntity.ok(ApiResponseModel.ok(user));
    }
}