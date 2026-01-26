package com.opentrace.server.repositories;


import com.opentrace.server.models.entities.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface SearchRepository extends JpaRepository<SearchEntity, UUID> {
}