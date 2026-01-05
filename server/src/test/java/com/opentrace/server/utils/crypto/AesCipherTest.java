package com.opentrace.server.utils.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AesCipherTest {

    private AesCipher aesCipher;
    private final byte[] testKey = new byte[32];
    private final byte[] testIv = new byte[16];

    @BeforeEach
    void setUp() {
        aesCipher = new AesCipher();
        java.util.Arrays.fill(testKey, (byte) 1);
        java.util.Arrays.fill(testIv, (byte) 2);
    }

    @Test
    void shouldEncryptData() throws Exception {
        String data = "test-data";

        String encrypted = aesCipher.encrypt(data, testKey, testIv);

        assertNotNull(encrypted);
        assertDoesNotThrow(() -> Base64.getDecoder().decode(encrypted));
    }

    @Test
    void shouldDecryptData() throws Exception {
        String originalData = "secret-message";
        String encrypted = aesCipher.encrypt(originalData, testKey, testIv);

        String decrypted = aesCipher.decrypt(encrypted, testKey, testIv);

        assertEquals(originalData, decrypted);
    }

    @Test
    void shouldThrowExceptionWhenKeyIsInvalid() {
        byte[] invalidKey = new byte[10];
        String data = "some-data";

        assertThrows(Exception.class, () -> aesCipher.encrypt(data, invalidKey, testIv));
    }

    @Test
    void shouldThrowExceptionWhenIvIsInvalid() {
        byte[] invalidIv = new byte[8];
        String data = "some-data";

        assertThrows(Exception.class, () -> aesCipher.encrypt(data, testKey, invalidIv));
    }

    @Test
    void shouldThrowExceptionWhenDecryptingInvalidBase64() {
        String invalidBase64 = "not-base64-content!";

        assertThrows(Exception.class, () -> aesCipher.decrypt(invalidBase64, testKey, testIv));
    }

    @Test
    void shouldProduceDifferentResultForDifferentIv() throws Exception {
        String data = "constant-data";
        byte[] differentIv = new byte[16];
        java.util.Arrays.fill(differentIv, (byte) 9);

        String result1 = aesCipher.encrypt(data, testKey, testIv);
        String result2 = aesCipher.encrypt(data, testKey, differentIv);

        assertNotEquals(result1, result2);
    }
}