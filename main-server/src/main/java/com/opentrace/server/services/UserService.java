package com.opentrace.server.services;

import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.repositories.UserRepository;
import com.opentrace.server.utils.builders.UserEntityBuilder;
import com.opentrace.shared.crypto.Hasher;
import com.opentrace.shared.crypto.RsaCipher;
import com.opentrace.server.utils.generators.AesGenerator;
import com.opentrace.shared.utils.mappers.Base64Mapper;
import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.utils.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;


@Service
@RequiredArgsConstructor
public class UserService {


    private final Base64Mapper base64Mapper;
    private final UserMapper userMapper;

    private final AesGenerator aesGenerator;
    private final RsaCipher rsaCipher;
    private final Hasher hasher;

    private final UserEntityBuilder userEntityBuilder;

    private final UserRepository userRepository;


    public UserDto getByGoogleSub(String encryptedSub) {
        return userRepository.findByGoogleSub(encryptedSub)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDto save(GoogleUserDto googleUser, String publicKey) {
        return userRepository.findByGoogleSub(hasher.hash(googleUser.getSub()))
                .map(
                        userMapper::toDto
                )
                .orElseGet(() -> {
                    byte[] aesKey = aesGenerator.generateAesKey();
                    byte[] aesIv = aesGenerator.generateIv();

                    byte[] encryptedAesKey = rsaCipher.encrypt(aesKey, publicKey);
                    String encryptedAesKeyBase64 = base64Mapper.toBase64(encryptedAesKey);

                    UserEntity savedUser = userEntityBuilder.createEncryptedEntity(
                            googleUser,
                            encryptedAesKeyBase64,
                            aesKey,
                            aesIv,
                            publicKey
                    );

                    System.out.println("was saved");
                    return userMapper.toDto(userRepository.save(savedUser));
                });
    }

    @Transactional
    public UserDto updateByGoogleSub(String googleSub, UserDto userDto) {
        UserEntity userEntity = userRepository.findByGoogleSub(googleSub)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with sub: " + googleSub));

        if (userDto.getEmail() != null) userEntity.setEmail(userDto.getEmail());
        if (userDto.getName() != null) userEntity.setName(userDto.getName());
        if (userDto.getAvatarUrl() != null) userEntity.setAvatarUrl(userDto.getAvatarUrl());
        if (userDto.getStatus() != null) userEntity.setStatus(userDto.getStatus());
        if (userDto.getPriority() != null) userEntity.setPriority(userDto.getPriority());
        if (userDto.getTokenVersion() != null) userEntity.setTokenVersion(userDto.getTokenVersion());

        return userMapper.toDto(userRepository.save(userEntity));
    }

    public UserDto getFieldByGoogleSub(String googleSub, BiConsumer<UserDto, UserEntity> mapper) {
        return userRepository.findByGoogleSub(googleSub)
                .map(entity -> {
                    UserDto dto = new UserDto();
                    mapper.accept(dto, entity);
                    return dto;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}