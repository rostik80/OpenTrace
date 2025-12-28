package com.opentrace.server.repositories;

import com.opentrace.server.models.entities.SearchRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SearchRequestRepository extends JpaRepository<SearchRequestEntity, UUID> {

}