package com.opentrace.server.utils.builders;

import com.opentrace.server.models.entities.SearchTaskEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SearchTaskEntityBuilder {

    public SearchTaskEntity createInitialTask(UUID searchId, String data) {
        return SearchTaskEntity.builder()
                .searchId(searchId)
                .parentId(null)
                .status("PENDING")
                .data(data)
                .build();
    }

    public SearchTaskEntity createSubTask(UUID searchId, UUID parentId, String data) {
        return SearchTaskEntity.builder()
                .searchId(searchId)
                .parentId(parentId)
                .status("PENDING")
                .data(data)
                .build();
    }
}