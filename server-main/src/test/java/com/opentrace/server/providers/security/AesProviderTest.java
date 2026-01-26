package com.opentrace.server.providers.security;

import com.opentrace.server.properties.AesProperties;
import com.opentrace.server.utils.generators.AesGenerator;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("Should load IV from properties when provided")
    void shouldLoadIvFromPropertiesWhenProvided() throws Exception {
        byte[] expectedIv = new byte[16];
        java.util.Arrays.fill(expectedIv, (byte) 2);
        String ivBase64 = Base64.getEncoder().encodeToString(expectedIv);

        when(aesProperties.getIv()).thenReturn(ivBase64);

        aesProvider.init();

        assertArrayEquals(expectedIv, aesProvider.getIv());
        verifyNoInteractions(aesGenerator);
    }

    @Test
    @DisplayName("Should generate temporary IV when properties are missing")
    void shouldGenerateTemporaryIvWhenPropertiesAreMissing() throws Exception {
        byte[] generatedIv = new byte[16];
        java.util.Arrays.fill(generatedIv, (byte) 4);

        when(aesProperties.getIv()).thenReturn(null);
        when(aesGenerator.generateIv()).thenReturn(generatedIv);

        aesProvider.init();

        assertArrayEquals(generatedIv, aesProvider.getIv());
        verify(aesGenerator).generateIv();
    }

    @Test
    @DisplayName("Should generate temporary IV when property is blank")
    void shouldGenerateTemporaryIvWhenPropertyIsBlank() throws Exception {
        byte[] generatedIv = new byte[16];

        when(aesProperties.getIv()).thenReturn("  ");
        when(aesGenerator.generateIv()).thenReturn(generatedIv);

        aesProvider.init();

        assertArrayEquals(generatedIv, aesProvider.getIv());
        verify(aesGenerator).generateIv();
    }
}