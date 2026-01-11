package com.opentrace.server.models.dto;

import lombok.Data;
import java.util.UUID;


@Data
public class SearchRequestDto {

    private UUID id;
    private String googleID;
    private String name;
    private String query;
    private String status;
}
