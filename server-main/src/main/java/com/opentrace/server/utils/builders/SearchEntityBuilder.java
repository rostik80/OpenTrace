package com.opentrace.server.utils.builders;

import com.opentrace.server.models.entities.SearchEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SearchEntityBuilder {

    public SearchEntity createEntity(String googleSub, String data) {
        return SearchEntity.builder()
                .googleSub(googleSub)
                .data(data)
                .status("PENDING")
                .createdAt(Instant.now())
                .build();
    }
}