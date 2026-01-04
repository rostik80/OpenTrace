package com.opentrace.server.repositories;

import com.opentrace.server.models.entities.RolePermissionEntity;
import com.opentrace.server.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
    List<RolePermissionEntity> findAllByUser(UserEntity user);
}