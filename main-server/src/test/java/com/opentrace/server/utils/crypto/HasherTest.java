package com.opentrace.server.utils.crypto;

import com.opentrace.shared.crypto.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {

    private Hasher hasher;

    @BeforeEach
    void setUp() {
        hasher = new Hasher();
    }

    @Test
    @DisplayName("Should return correct SHA-256 hash in hex format")
    void shouldReturnCorrectHash() {
        String input = "hello-world";
        String expected = "afa27b44d43b02a9fea41d13cedc2e4016cfcf87c5dbf990e593669aa8ce286d";

        String result = hasher.hash(input);

        assertEquals(expected, result);
        assertEquals(64, result.length());
    }

    @Test
    @DisplayName("Should be deterministic (same input produces same hash)")
    void shouldBeDeterministic() {
        String input = "consistent-data";

        String firstResult = hasher.hash(input);
        String secondResult = hasher.hash(input);

        assertEquals(firstResult, secondResult);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when input is null")
    void shouldThrowExceptionOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> hasher.hash(null));
    }

    @Test
    @DisplayName("Should handle empty string correctly")
    void shouldHashEmptyString() {
        String input = "";
        String expectedEmptyHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        String result = hasher.hash(input);

        assertEquals(expectedEmptyHash, result);
    }
}