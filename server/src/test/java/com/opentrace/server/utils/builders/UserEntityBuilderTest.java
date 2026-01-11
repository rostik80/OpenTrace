package com.opentrace.server.utils.builders;

import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.utils.crypto.AesCipher;
import com.opentrace.server.utils.crypto.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEntityBuilderTest {

    @Mock
    private AesCipher aesCipher;

    @Mock
    private Hasher hasher;

    private UserEntityBuilder userEntityBuilder;

    @BeforeEach
    void setUp() {
        userEntityBuilder = new UserEntityBuilder(aesCipher, hasher);
    }

    @Test
    @DisplayName("Should correctly map GoogleUserDTO to encrypted UserEntity")
    void shouldCreateEncryptedEntity() throws Exception {
        GoogleUserDTO googleUser = new GoogleUserDTO();
        googleUser.setSub("google-sub-id");
        googleUser.setEmail("test@example.com");
        googleUser.setGivenName("John");
        googleUser.setPicture("http://picture.url");

        byte[] aesKey = new byte[16];
        byte[] aesIv = new byte[16];
        String encryptedAesKey = "encrypted-key-base64";
        String publicKey = "public-key-base64";

        String hashedSub = "hashed-sub";
        String encryptedEmail = "enc-email";
        String encryptedName = "enc-name";
        String encryptedPicture = "enc-picture";

        when(hasher.hash(googleUser.getSub())).thenReturn(hashedSub);
        when(aesCipher.encrypt(googleUser.getEmail(), aesKey, aesIv)).thenReturn(encryptedEmail);
        when(aesCipher.encrypt(googleUser.getGivenName(), aesKey, aesIv)).thenReturn(encryptedName);
        when(aesCipher.encrypt(googleUser.getPicture(), aesKey, aesIv)).thenReturn(encryptedPicture);

        UserEntity result = userEntityBuilder.createEncryptedEntity(
                googleUser, encryptedAesKey, aesKey, aesIv, publicKey
        );

        assertNotNull(result);

        assertEquals(hashedSub, ReflectionTestUtils.getField(result, "googleSub"));
        assertEquals(encryptedEmail, ReflectionTestUtils.getField(result, "email"));
        assertEquals(encryptedName, ReflectionTestUtils.getField(result, "name"));
        assertEquals(encryptedPicture, ReflectionTestUtils.getField(result, "avatarUrl"));
        assertEquals("ACTIVE", ReflectionTestUtils.getField(result, "status"));
        assertEquals(encryptedAesKey, ReflectionTestUtils.getField(result, "aesEncryptedKey"));
        assertEquals(publicKey, ReflectionTestUtils.getField(result, "rsaPublicKey"));
    }

    @Test
    @DisplayName("Should throw RuntimeException when encryption fails")
    void shouldThrowExceptionOnEncryptionFailure() throws Exception {
        GoogleUserDTO googleUser = new GoogleUserDTO();
        googleUser.setSub("sub");
        googleUser.setEmail("email");

        byte[] key = new byte[16];
        byte[] iv = new byte[16];

        when(hasher.hash("sub")).thenReturn("hash");
        when(aesCipher.encrypt("email", key, iv)).thenThrow(new RuntimeException("AES Error"));

        assertThrows(RuntimeException.class, () ->
                userEntityBuilder.createEncryptedEntity(googleUser, "key", key, iv, "pub")
        );
    }
}