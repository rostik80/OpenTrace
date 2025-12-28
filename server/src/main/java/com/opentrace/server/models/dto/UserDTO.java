package com.opentrace.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String googleId;
    private String email;
    private String name;
    private String avatarUrl;
    private String role;
    private String status;
}