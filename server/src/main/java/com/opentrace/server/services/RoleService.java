package com.opentrace.server.services;

import com.opentrace.server.models.entities.RoleEntity;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.RoleRepository;
import com.opentrace.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

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

    public void assignDefaultRoles(UserEntity user) {
        RoleEntity defaultRole = RoleEntity.builder()
                .roleName("REQUESTER")
                .user(user)
                .build();

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(defaultRole);

        roleRepository.save(defaultRole);
    }
}
