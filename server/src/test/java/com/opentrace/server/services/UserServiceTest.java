package com.opentrace.server.services;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.UserRepository;
import com.opentrace.server.utils.mappers.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should update existing user when google sub is found")
    void shouldUpdateExistingUser() {

        GoogleUserDTO googleUser = new GoogleUserDTO();
        googleUser.setSub("google-123");
        googleUser.setName("New Name");

        UserEntity existingUser = new UserEntity();
        existingUser.setGoogleSub("google-123");
        existingUser.setName("Old Name");

        when(userRepository.findByGoogleSub("google-123")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(new UserDTO());

        userService.saveOrUpdate(googleUser);

        assertEquals("New Name", existingUser.getName());
        verify(userRepository).save(existingUser);

        verify(rolePermissionService, never()).assignRolesPermission(any());
    }

    @Test
    @DisplayName("Should create new user and assign roles when not found")
    void shouldCreateNewUser() {

        GoogleUserDTO googleUser = new GoogleUserDTO();
        googleUser.setSub("new-sub");

        UserEntity newUser = new UserEntity();
        UserDTO savedDto = new UserDTO();

        when(userRepository.findByGoogleSub("new-sub")).thenReturn(Optional.empty());
        when(userMapper.toEntity(googleUser)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(userMapper.toDto(newUser)).thenReturn(savedDto);

        UserDTO result = userService.saveOrUpdate(googleUser);

        assertEquals(1, newUser.getTokenVersion());
        assertEquals(0, newUser.getPriority());
        verify(userRepository).save(newUser);

        verify(rolePermissionService).assignRolesPermission(savedDto);
    }
}