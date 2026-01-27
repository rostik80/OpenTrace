package com.opentrace.server.utils.crypto;

import com.opentrace.shared.utils.mappers.Base64Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RsaCipherTest {

    @Mock
    private Base64Mapper base64Mapper;

    private com.opentrace.shared.crypto.RsaCipher rsaCipher;

    private String publicKeyBase64;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws Exception {
        rsaCipher = new com.opentrace.shared.crypto.RsaCipher(base64Mapper);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();

        publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        when(base64Mapper.toPublicKey(publicKeyBase64)).thenReturn(keyPair.getPublic());
    }

    @Test
    @DisplayName("Should successfully encrypt data using public key string")
    void shouldEncryptData() {
        byte[] originalData = "test-rsa-payload".getBytes();

        byte[] encrypted = rsaCipher.encrypt(originalData, publicKeyBase64);

        assertNotNull(encrypted);
        assertNotEquals(new String(originalData), new String(encrypted));
    }

    @Test
    @DisplayName("Should throw RuntimeException when data is too large for RSA")
    void shouldThrowExceptionWhenDataIsTooLarge() {
        byte[] largeData = new byte[512];

        assertThrows(RuntimeException.class, () -> rsaCipher.encrypt(largeData, publicKeyBase64));
    }

    @Test
    @DisplayName("Should handle empty byte array during encryption")
    void shouldHandleEmptyByteArray() {
        byte[] emptyData = new byte[0];

        byte[] encrypted = rsaCipher.encrypt(emptyData, publicKeyBase64);

        assertNotNull(encrypted);
    }
}