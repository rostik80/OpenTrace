package com.opentrace.server.repositories;


import com.opentrace.server.models.entities.SearchTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface SearchTaskRepository extends JpaRepository<SearchTaskEntity, UUID> {
    List<SearchTaskEntity> findAllByStatus(String status);
}