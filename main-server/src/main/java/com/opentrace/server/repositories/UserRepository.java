package com.opentrace.server.repositories;

import com.opentrace.server.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByGoogleSub(String googleSub);
    Optional<UserEntity> findByEmail(String email);
}