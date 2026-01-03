package com.opentrace.server.services;

import com.opentrace.server.utils.mappers.UserMapper;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.RolePermissionEntity;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.RolePermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final UserMapper userMapper;


    public List<String> getAllowedRoles(UserDTO userDto) {
        UserEntity user = userMapper.toEntity(userDto);

        return rolePermissionRepository.findAllByUser(user)
                .stream()
                .map(RolePermissionEntity::getTargetRole)
                .toList();
    }


    @Transactional
    public void assignRolesPermission(UserDTO userDto, String... roles) {
        UserEntity user = userMapper.toEntity(userDto);

        List<String> rolesToGrant = (roles == null || roles.length == 0)
                ? List.of("WORKER", "REQUESTER")
                : Arrays.asList(roles);

        List<RolePermissionEntity> permissions = rolesToGrant.stream()
                .map(roleName -> RolePermissionEntity.builder()
                        .user(user)
                        .targetRole(roleName)
                        .build())
                .toList();

        rolePermissionRepository.saveAll(permissions);
    }
}
