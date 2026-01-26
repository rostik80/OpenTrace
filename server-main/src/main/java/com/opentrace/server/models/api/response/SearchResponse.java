package com.opentrace.server.models.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private UUID id;
    private String status;
    private String intent;
    private Instant createdAt;
    private List<TaskItemResponse> tasks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskItemResponse {
        private String workerType;
        private String status;
        private String result;
        private String error;
    }
}