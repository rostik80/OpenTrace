package com.opentrace.server.utils.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class Base64MapperTest {

    private com.opentrace.shared.utils.mappers.Base64Mapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<com.opentrace.shared.utils.mappers.Base64Mapper> constructor = com.opentrace.shared.utils.mappers.Base64Mapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        mapper = constructor.newInstance();
    }

    @Test
    @DisplayName("Should convert valid Base64 string to RSA PublicKey")
    void shouldConvertToPublicKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        PublicKey originalKey = keyGen.generateKeyPair().getPublic();
        String base64Key = Base64.getEncoder().encodeToString(originalKey.getEncoded());

        PublicKey result = mapper.toPublicKey(base64Key);

        assertNotNull(result);
        assertEquals("RSA", result.getAlgorithm());
        assertArrayEquals(originalKey.getEncoded(), result.getEncoded());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for invalid RSA key")
    void shouldThrowExceptionForInvalidKey() {
        String invalidKey = "not-a-key";
        assertThrows(IllegalArgumentException.class, () -> mapper.toPublicKey(invalidKey));
    }

    @Test
    @DisplayName("Should convert string to bytes")
    void shouldConvertToBytes() {
        String input = "SGVsbG8=";
        byte[] expected = "Hello".getBytes();

        byte[] result = com.opentrace.shared.utils.mappers.Base64Mapper.toBytes(input);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Should return null when converting null string to bytes")
    void shouldReturnNullForToBytes() {
        assertNull(com.opentrace.shared.utils.mappers.Base64Mapper.toBytes(null));
    }

    @Test
    @DisplayName("Should convert bytes to Base64 string")
    void shouldConvertToBase64() {
        byte[] input = "Hello".getBytes();
        String expected = "SGVsbG8=";

        String result = com.opentrace.shared.utils.mappers.Base64Mapper.toBase64(input);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should return null when converting null bytes to string")
    void shouldReturnNullForToBase64() {
        assertNull(com.opentrace.shared.utils.mappers.Base64Mapper.toBase64(null));
    }
}