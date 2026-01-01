package com.opentrace.server.services;

import com.opentrace.server.mappers.UserMapper;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.RoleEntity;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.RoleRepository;
import com.opentrace.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolePermissionService rolePermissionService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<String> getUserRoles(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return List.of("REQUESTER");
        }

        return user.getRoles().stream()
                .map(RoleEntity::getRoleName)
                .toList();
    }

    @Transactional
    public void assignRoles(UserDTO userDto, List<String> roles) {
        if (roles == null || roles.isEmpty()) return;

        List<String> allowedRoles = rolePermissionService.getAllowedRoles(userDto);

        List<String> alreadyAssignedRoles = roleRepository.findAllByUserId(userDto.getId())
                .stream()
                .map(RoleEntity::getRoleName)
                .toList();

        UserEntity user = userMapper.toEntity(userDto);

        List<RoleEntity> rolesToSave = roles.stream()
                .filter(allowedRoles::contains)
                .filter(role -> !alreadyAssignedRoles.contains(role))
                .map(roleName -> RoleEntity.builder()
                        .roleName(roleName)
                        .user(user)
                        .build())
                .toList();

        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
        }
    }

    @Transactional
    public void assignDefaultRoles(UserDTO userDTO, String... roles) {
        UserEntity user = userMapper.toEntity(userDTO);

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        List<RoleEntity> rolesToSave = Arrays.stream(roles)
                .map(roleName -> RoleEntity.builder()
                        .roleName(roleName)
                        .user(user)
                        .build())
                .peek(user.getRoles()::add)
                .toList();

        roleRepository.saveAll(rolesToSave);
    }
}
