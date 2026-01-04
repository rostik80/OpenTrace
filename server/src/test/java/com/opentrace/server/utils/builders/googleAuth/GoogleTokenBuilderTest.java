package com.opentrace.server.utils.builders.googleAuth;

import com.opentrace.server.models.dto.GoogleTokenRequestDTO;
import com.opentrace.server.properties.GoogleAuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleTokenBuilderTest {

    @Mock
    private GoogleAuthProperties props;

    private GoogleTokenBuilder googleTokenBuilder;

    @BeforeEach
    void setUp() {
        googleTokenBuilder = new GoogleTokenBuilder(props);
    }

    @Test
    @DisplayName("Should correctly build GoogleTokenRequestDTO with data from properties")
    void shouldBuildTokenBodyRequest() {
        String testCode = "4/0AdQt8qh-test-code";
        String expectedClientId = "test-client-id";
        String expectedSecret = "test-client-secret";
        String expectedRedirectUri = "http://localhost:8080/callback";

        when(props.getClientId()).thenReturn(expectedClientId);
        when(props.getClientSecret()).thenReturn(expectedSecret);
        when(props.getRedirectUri()).thenReturn(expectedRedirectUri);

        GoogleTokenRequestDTO result = googleTokenBuilder.buildTokenBodyRequest(testCode);

        assertNotNull(result, "Resulting DTO should not be null");
        assertEquals(testCode, result.getCode());
        assertEquals(expectedClientId, result.getClient_id());
        assertEquals(expectedSecret, result.getClient_secret());
        assertEquals(expectedRedirectUri, result.getRedirect_uri());
        assertEquals("authorization_code", result.getGrant_type(), "Grant type must be hardcoded as 'authorization_code'");
    }
}