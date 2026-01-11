package com.opentrace.server.utils.codecs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GoogleStatePackerTest {

    private GoogleStatePacker packer;

    @BeforeEach
    void setUp() {
        packer = new GoogleStatePacker();
    }

    @Test
    @DisplayName("Should encode roles and publicKey into Base64Url string")
    void shouldEncodeCorrectly() {
        String roles = "ADMIN,USER";
        String publicKey = "key123";

        String result = packer.encode(roles, publicKey);

        assertNotNull(result);
        assertFalse(result.contains("="));
        assertFalse(result.contains("+"));
        assertFalse(result.contains("/"));
    }

    @Test
    @DisplayName("Should decode valid state string back to StateData")
    void shouldDecodeCorrectly() {
        String roles = "WORKER";
        String publicKey = "public-key-value";
        String encoded = packer.encode(roles, publicKey);

        GoogleStatePacker.StateData result = packer.decode(encoded);

        assertEquals(roles, result.roles());
        assertEquals(publicKey, result.publicKey());
    }

    @Test
    @DisplayName("Should handle null values during encoding")
    void shouldHandleNullsInEncode() {
        String result = packer.encode(null, null);
        GoogleStatePacker.StateData decoded = packer.decode(result);

        assertEquals("", decoded.roles());
        assertEquals("", decoded.publicKey());
    }

    @ParameterizedTest
    @CsvSource({
            "ROLE_USER|key, ROLE_USER, key",
            "|, '', ''",
            "ONLY_ROLE|, ONLY_ROLE, ''",
            "|ONLY_KEY, '', ONLY_KEY"
    })
    @DisplayName("Should correctly split different string combinations")
    void shouldHandleVariousStringParts(String raw, String expectedRoles, String expectedKey) {
        String encoded = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes());

        GoogleStatePacker.StateData result = packer.decode(encoded);

        assertEquals(expectedRoles, result.roles());
        assertEquals(expectedKey, result.publicKey());
    }

    @Test
    @DisplayName("Should return empty data for invalid Base64 input")
    void shouldReturnEmptyForInvalidBase64() {
        GoogleStatePacker.StateData result = packer.decode("not-a-base64-string!@#");

        assertEquals("", result.roles());
        assertNull(result.publicKey());
    }

    @Test
    @DisplayName("Should return empty data for null or empty input")
    void shouldReturnEmptyForNullOrEmpty() {
        GoogleStatePacker.StateData nullResult = packer.decode(null);
        GoogleStatePacker.StateData emptyResult = packer.decode("");

        assertEquals("", nullResult.roles());
        assertNull(nullResult.publicKey());
        assertEquals("", emptyResult.roles());
        assertNull(emptyResult.publicKey());
    }

    @Test
    @DisplayName("Should handle complex public keys with special characters")
    void shouldHandleSpecialCharacters() {
        String roles = "USER";
        String publicKey = "key/with+special=chars";

        String encoded = packer.encode(roles, publicKey);
        GoogleStatePacker.StateData result = packer.decode(encoded);

        assertEquals(roles, result.roles());
        assertEquals(publicKey, result.publicKey());
    }
}