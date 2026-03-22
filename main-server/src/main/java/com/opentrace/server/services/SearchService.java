package com.opentrace.server.services;

import com.opentrace.server.models.api.request.SearchRequest;
import com.opentrace.shared.models.api.response.SearchStatusResponse;
import com.opentrace.server.models.entities.SearchEntity;
import com.opentrace.server.models.entities.SearchTaskEntity;
import com.opentrace.server.repositories.SearchRepository;
import com.opentrace.server.repositories.SearchTaskRepository;
import com.opentrace.server.utils.builders.SearchEntityBuilder;
import com.opentrace.server.utils.builders.SearchTaskEntityBuilder;
import com.opentrace.server.utils.mappers.SearchMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final SearchTaskRepository searchTaskRepository;
    private final SearchEntityBuilder searchEntityBuilder;
    private final SearchTaskEntityBuilder searchTaskEntityBuilder;
    private final SearchMapper searchMapper;

    @Transactional
    public SearchStatusResponse createSearch(SearchRequest request) {
        String googleSub = SecurityContextHolder.getContext().getAuthentication().getName();

        SearchEntity entity = searchEntityBuilder.createEntity(googleSub, request.getQuery());
        SearchEntity savedEntity = searchRepository.save(entity);

        SearchTaskEntity initialTask = searchTaskEntityBuilder.createInitialTask(savedEntity.getId(), savedEntity.getData());
        searchTaskRepository.save(initialTask);

        return getSearchStatus(savedEntity.getId());
    }

    @Transactional(readOnly = true)
    public SearchStatusResponse getSearchStatus(UUID id) {
        return searchRepository.findById(id)
                .map(searchMapper::toStatusResponse)
                .orElseThrow(() -> new EntityNotFoundException("Search not found with id: " + id));
    }
}