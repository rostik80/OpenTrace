package com.opentrace.server.utils.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class AesGeneratorTest {

    private AesGenerator aesGenerator;

    @BeforeEach
    void setUp() {
        aesGenerator = new AesGenerator();
    }

    @Test
    void generateAesKey_ShouldReturn256BitKey() throws Exception {
        byte[] key = aesGenerator.generateAesKey();

        assertNotNull(key);
        assertEquals(32, key.length);
    }

    @Test
    void generateIv_ShouldReturn16ByteIv() {
        byte[] iv = aesGenerator.generateIv();

        assertNotNull(iv);
        assertEquals(16, iv.length);
    }

    @Test
    void generateIv_ShouldProduceDifferentValuesEachTime() {
        byte[] iv1 = aesGenerator.generateIv();
        byte[] iv2 = aesGenerator.generateIv();

        assertArrayEquals(iv1, iv1);
        assertFalse(java.util.Arrays.equals(iv1, iv2));
    }

    @Test
    void generateRsaKeyPair_ShouldReturnValidKeyPair() throws Exception {
        KeyPair keyPair = aesGenerator.generateRsaKeyPair();

        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivate());
        assertNotNull(keyPair.getPublic());
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }
}