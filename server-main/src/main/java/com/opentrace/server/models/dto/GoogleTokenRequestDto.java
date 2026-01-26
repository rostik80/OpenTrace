package com.opentrace.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenRequestDto {

    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;
}