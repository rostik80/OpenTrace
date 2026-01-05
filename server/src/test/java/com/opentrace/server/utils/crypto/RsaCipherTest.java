package com.opentrace.server.utils.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class RsaCipherTest {

    private RsaCipher rsaCipher;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @BeforeEach
    void setUp() throws Exception {
        rsaCipher = new RsaCipher();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
    }

    @Test
    void shouldEncryptAndDecryptData() throws Exception {
        byte[] originalData = "test-rsa-payload".getBytes();

        byte[] encrypted = rsaCipher.encrypt(originalData, publicKey);
        byte[] decrypted = rsaCipher.decrypt(encrypted, privateKey);

        assertNotNull(encrypted);
        assertNotEquals(new String(originalData), new String(encrypted));
        assertArrayEquals(originalData, decrypted);
    }

    @Test
    void shouldThrowExceptionWhenDecryptingWithWrongKey() throws Exception {
        byte[] data = "secret".getBytes();
        byte[] encrypted = rsaCipher.encrypt(data, publicKey);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        PrivateKey wrongPrivateKey = keyGen.generateKeyPair().getPrivate();

        assertThrows(Exception.class, () -> rsaCipher.decrypt(encrypted, wrongPrivateKey));
    }

    @Test
    void shouldThrowExceptionWhenDataIsTooLargeForKeySize() {
        byte[] largeData = new byte[512];

        assertThrows(Exception.class, () -> rsaCipher.encrypt(largeData, publicKey));
    }

    @Test
    void shouldHandleEmptyByteArray() throws Exception {
        byte[] emptyData = new byte[0];

        byte[] encrypted = rsaCipher.encrypt(emptyData, publicKey);
        byte[] decrypted = rsaCipher.decrypt(encrypted, privateKey);

        assertArrayEquals(emptyData, decrypted);
    }
}