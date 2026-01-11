package com.opentrace.server.services;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.RolePermissionEntity;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.RolePermissionRepository;
import com.opentrace.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;

    public List<String> getAllowedRoles(UserDTO userDto) {
        return rolePermissionRepository.findAllByUserId(userDto.getId())
                .stream()
                .map(RolePermissionEntity::getTargetRole)
                .toList();
    }

    @Transactional
    public void assignRolesPermission(UserDTO userDto, String... roles) {
        UserEntity user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> rolesToRequest = (roles == null || roles.length == 0)
                ? List.of("WORKER", "REQUESTER")
                : Arrays.asList(roles);

        List<String> existingRoles = rolePermissionRepository.findAllByUser(user)
                .stream()
                .map(RolePermissionEntity::getTargetRole)
                .toList();

        List<RolePermissionEntity> newPermissions = rolesToRequest.stream()
                .filter(role -> !existingRoles.contains(role))
                .map(roleName -> RolePermissionEntity.builder()
                        .user(user)
                        .targetRole(roleName)
                        .build())
                .toList();

        if (!newPermissions.isEmpty()) {
            rolePermissionRepository.saveAll(newPermissions);
        }
    }
}