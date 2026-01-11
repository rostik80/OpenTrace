package com.opentrace.server.services;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.RolePermissionEntity;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.RolePermissionRepository;
import com.opentrace.server.utils.mappers.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolePermissionServiceTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RolePermissionService rolePermissionService;

    @Test
    @DisplayName("Should return allowed roles for user")
    void shouldGetAllowedRoles() {

        UserDTO dto = new UserDTO();
        UserEntity entity = new UserEntity();
        RolePermissionEntity perm = RolePermissionEntity.builder().targetRole("ADMIN").build();

        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(rolePermissionRepository.findAllByUser(entity)).thenReturn(List.of(perm));

        List<String> roles = rolePermissionService.getAllowedRoles(dto);

        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0));
    }

    @Test
    @DisplayName("Should assign default roles when no roles provided")

    void shouldAssignDefaultRoles() {

        UserDTO dto = new UserDTO();
        UserEntity entity = new UserEntity();
        when(userMapper.toEntity(dto)).thenReturn(entity);

        rolePermissionService.assignRolesPermission(dto);

        ArgumentCaptor<List<RolePermissionEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rolePermissionRepository).saveAll(captor.capture());

        List<RolePermissionEntity> savedPermissions = captor.getValue();
        assertEquals(2, savedPermissions.size());

        List<String> roleNames = savedPermissions.stream().map(RolePermissionEntity::getTargetRole).toList();
        assertTrue(roleNames.containsAll(List.of("WORKER", "REQUESTER")));
        assertEquals(entity, savedPermissions.get(0).getUser());
    }

    @Test
    @DisplayName("Should assign specific roles when provided")

    void shouldAssignSpecificRoles() {

        UserDTO dto = new UserDTO();
        UserEntity entity = new UserEntity();
        when(userMapper.toEntity(dto)).thenReturn(entity);

        rolePermissionService.assignRolesPermission(dto, "MANAGER", "DIRECTOR");

        ArgumentCaptor<List<RolePermissionEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(rolePermissionRepository).saveAll(captor.capture());

        List<RolePermissionEntity> savedPermissions = captor.getValue();
        assertEquals(2, savedPermissions.size());

        List<String> roleNames = savedPermissions.stream().map(RolePermissionEntity::getTargetRole).toList();
        assertTrue(roleNames.containsAll(List.of("MANAGER", "DIRECTOR")));
        assertFalse(roleNames.contains("WORKER"), "Should not contain default roles when specific are provided");
    }
}