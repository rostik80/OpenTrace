package com.opentrace.server.utils.builders;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.utils.crypto.AesCipher;
import com.opentrace.server.utils.crypto.Hasher;
import com.opentrace.server.utils.mappers.Base64Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserEntityBuilder {

    private final AesCipher aesCipher;
    private final Hasher hasher;

    public UserEntity createEncryptedEntity(GoogleUserDTO googleUser, String encryptedAesKeyBase64, byte[] aesKey, byte[] aesIv, String publicKey) {

        try {
            return new UserEntity(
                    null,
                    hasher.hash(googleUser.getSub()),
                    aesCipher.encrypt(googleUser.getEmail(), aesKey, aesIv),
                    aesCipher.encrypt(googleUser.getGivenName(), aesKey, aesIv),
                    aesCipher.encrypt(googleUser.getPicture(), aesKey, aesIv),
                    "ACTIVE",
                    0,
                    1,
                    encryptedAesKeyBase64,
                    Base64Mapper.toBase64(aesIv),
                    publicKey
            );
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}