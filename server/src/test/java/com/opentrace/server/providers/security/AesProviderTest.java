package com.opentrace.server.providers.security;

import com.opentrace.server.properties.AesProperties;
import com.opentrace.server.utils.generators.AesGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AesProviderTest {

    @Mock
    private AesGenerator aesGenerator;

    @Mock
    private AesProperties aesProperties;

    @InjectMocks
    private AesProvider aesProvider;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldLoadKeysFromPropertiesWhenProvided() throws Exception {
        byte[] expectedKey = new byte[32];
        byte[] expectedIv = new byte[16];
        java.util.Arrays.fill(expectedKey, (byte) 1);
        java.util.Arrays.fill(expectedIv, (byte) 2);

        String keyBase64 = Base64.getEncoder().encodeToString(expectedKey);
        String ivBase64 = Base64.getEncoder().encodeToString(expectedIv);

        when(aesProperties.getKey()).thenReturn(keyBase64);
        when(aesProperties.getIv()).thenReturn(ivBase64);

        aesProvider.init();

        assertArrayEquals(expectedKey, aesProvider.getKey());
        assertArrayEquals(expectedIv, aesProvider.getIv());
        verifyNoInteractions(aesGenerator);
    }

    @Test
    void shouldGenerateTemporaryKeysWhenPropertiesAreMissing() throws Exception {
        byte[] generatedKey = new byte[32];
        byte[] generatedIv = new byte[16];
        java.util.Arrays.fill(generatedKey, (byte) 3);
        java.util.Arrays.fill(generatedIv, (byte) 4);

        when(aesProperties.getKey()).thenReturn(null);
        when(aesProperties.getIv()).thenReturn("");
        when(aesGenerator.generateAesKey()).thenReturn(generatedKey);
        when(aesGenerator.generateIv()).thenReturn(generatedIv);

        aesProvider.init();

        assertArrayEquals(generatedKey, aesProvider.getKey());
        assertArrayEquals(generatedIv, aesProvider.getIv());
        verify(aesGenerator).generateAesKey();
        verify(aesGenerator).generateIv();
    }

    @Test
    void shouldGenerateKeysWhenOnlyKeyIsMissing() throws Exception {
        when(aesProperties.getKey()).thenReturn(null);
        when(aesProperties.getIv()).thenReturn("some-iv");
        when(aesGenerator.generateAesKey()).thenReturn(new byte[32]);
        when(aesGenerator.generateIv()).thenReturn(new byte[16]);

        aesProvider.init();

        verify(aesGenerator).generateAesKey();
        verify(aesGenerator).generateIv();
    }

    @Test
    void shouldGenerateKeysWhenOnlyIvIsMissing() throws Exception {
        when(aesProperties.getKey()).thenReturn("some-key");
        when(aesProperties.getIv()).thenReturn("  ");
        when(aesGenerator.generateAesKey()).thenReturn(new byte[32]);
        when(aesGenerator.generateIv()).thenReturn(new byte[16]);

        aesProvider.init();

        verify(aesGenerator).generateAesKey();
        verify(aesGenerator).generateIv();
    }
}