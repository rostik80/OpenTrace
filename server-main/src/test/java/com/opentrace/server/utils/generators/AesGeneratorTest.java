package com.opentrace.server.utils.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AesGeneratorTest {

    private AesGenerator aesGenerator;

    @BeforeEach
    void setUp() {
        aesGenerator = new AesGenerator();
    }

    @Test
    @DisplayName("Should generate 256-bit AES key")
    void generateAesKey_ShouldReturn256BitKey() {
        byte[] key = aesGenerator.generateAesKey();

        assertNotNull(key);
        assertEquals(32, key.length);
    }

    @Test
    @DisplayName("Should generate 16-byte IV")
    void generateIv_ShouldReturn16ByteIv() {
        byte[] iv = aesGenerator.generateIv();

        assertNotNull(iv);
        assertEquals(16, iv.length);
    }

    @Test
    @DisplayName("Should produce unique IVs each time")
    void generateIv_ShouldProduceDifferentValuesEachTime() {
        byte[] iv1 = aesGenerator.generateIv();
        byte[] iv2 = aesGenerator.generateIv();

        assertNotNull(iv1);
        assertNotNull(iv2);
        assertFalse(java.util.Arrays.equals(iv1, iv2));
    }
}