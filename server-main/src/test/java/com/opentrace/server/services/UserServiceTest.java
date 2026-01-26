package com.opentrace.server.services;

import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.repositories.UserRepository;
import com.opentrace.server.utils.builders.UserEntityBuilder;
import com.opentrace.shared.crypto.Hasher;
import com.opentrace.shared.crypto.RsaCipher;
import com.opentrace.server.utils.generators.AesGenerator;
import com.opentrace.shared.utils.mappers.Base64Mapper;
import com.opentrace.server.utils.mappers.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    private Base64Mapper base64Mapper;
    @Mock
    private AesGenerator aesGenerator;
    @Mock
    private RsaCipher rsaCipher;
    @Mock
    private Hasher hasher;
    @Mock
    private UserEntityBuilder userEntityBuilder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return existing user by hashed google sub")
    void shouldGetByGoogleSub() {
        String encryptedSub = "hashed-sub";
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        when(userRepository.findByGoogleSub(encryptedSub)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.getByGoogleSub(encryptedSub);

        assertNotNull(result);
        verify(userRepository).findByGoogleSub(encryptedSub);
    }

    @Test
    @DisplayName("Should throw exception when user not found by sub")
    void shouldThrowExceptionWhenUserNotFound() {
        String encryptedSub = "non-existent";
        when(userRepository.findByGoogleSub(encryptedSub)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getByGoogleSub(encryptedSub));
    }

    @Test
    @DisplayName("Should return existing user DTO if hashed sub is found in database")
    void shouldReturnExistingUserWhenFound() {
        GoogleUserDto googleUser = new GoogleUserDto();
        googleUser.setSub("raw-sub");
        String hashedSub = "hashed-sub";
        String publicKey = "test-public-key";
        UserEntity existingUser = new UserEntity();
        UserDto expectedDto = new UserDto();

        when(hasher.hash("raw-sub")).thenReturn(hashedSub);
        when(userRepository.findByGoogleSub(hashedSub)).thenReturn(Optional.of(existingUser));
        when(userMapper.toDto(existingUser)).thenReturn(expectedDto);

        UserDto result = userService.save(googleUser, publicKey);

        assertNotNull(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create and encrypt new user when hashed sub is not found")
    void shouldCreateNewUserWhenNotFound() {
        GoogleUserDto googleUser = new GoogleUserDto();
        googleUser.setSub("new-sub");
        String hashedSub = "hashed-new-sub";
        String publicKey = "test-public-key";

        byte[] aesKey = new byte[32];
        byte[] aesIv = new byte[16];
        byte[] encryptedAesKey = new byte[256];
        String encryptedAesKeyBase64 = "encrypted-base64";

        UserEntity newUser = new UserEntity();
        UserDto savedDto = new UserDto();

        when(hasher.hash("new-sub")).thenReturn(hashedSub);
        when(userRepository.findByGoogleSub(hashedSub)).thenReturn(Optional.empty());
        when(aesGenerator.generateAesKey()).thenReturn(aesKey);
        when(aesGenerator.generateIv()).thenReturn(aesIv);
        when(rsaCipher.encrypt(any(byte[].class), anyString())).thenReturn(encryptedAesKey);
        when(base64Mapper.toBase64(any(byte[].class))).thenReturn(encryptedAesKeyBase64);

        doReturn(newUser).when(userEntityBuilder).createEncryptedEntity(
                any(GoogleUserDto.class),
                anyString(),
                any(byte[].class),
                any(byte[].class),
                anyString()
        );

        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);
        when(userMapper.toDto(any(UserEntity.class))).thenReturn(savedDto);

        UserDto result = userService.save(googleUser, publicKey);

        assertNotNull(result);
        verify(userRepository).save(any(UserEntity.class));
        verify(userEntityBuilder).createEncryptedEntity(any(), any(), any(), any(), any());
    }
}