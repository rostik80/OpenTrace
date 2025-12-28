package com.opentrace.server.models.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class SearchRequestDTO {

    private UUID id;
    private String googleID;
    private String name;
    private String query;
    private String status;
}
