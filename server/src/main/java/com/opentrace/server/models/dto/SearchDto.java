package com.opentrace.server.models.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class SearchDto {

    private UUID id;
    private String googleSub;
    private String status;
    private String intent;
    private Instant createdAt;
    private Instant updatedAt;
}