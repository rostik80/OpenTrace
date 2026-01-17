package com.opentrace.server.models.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class SearchTaskDto {

    private UUID id;
    private UUID searchId;
    private String status;
    private Instant startedAt;
    private Instant finishedAt;
    private String result;
    private String error;
}