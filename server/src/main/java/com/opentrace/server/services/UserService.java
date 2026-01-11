package com.opentrace.server.services;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.repositories.UserRepository;
import com.opentrace.server.utils.builders.UserEntityBuilder;
import com.opentrace.server.utils.crypto.Hasher;
import com.opentrace.server.utils.crypto.RsaCipher;
import com.opentrace.server.utils.generators.AesGenerator;
import com.opentrace.server.utils.mappers.Base64Mapper;
import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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


    public UserDTO getByGoogleSub(String encryptedSub) {
        return userRepository.findByGoogleSub(encryptedSub)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDTO save(GoogleUserDTO googleUser, String publicKey) {
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
}