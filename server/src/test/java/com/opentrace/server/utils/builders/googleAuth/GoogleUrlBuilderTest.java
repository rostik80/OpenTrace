package com.opentrace.server.utils.builders.googleAuth;

import com.opentrace.server.properties.GoogleAuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleUrlBuilderTest {

    @Mock
    private GoogleAuthProperties props;

    private GoogleUrlBuilder googleUrlBuilder;

    private final String CLIENT_ID = "test-client-id";
    private final String REDIRECT_URI = "http://localhost:8080/callback";

    @BeforeEach
    void setUp() {
        googleUrlBuilder = new GoogleUrlBuilder(props);

        when(props.getClientId()).thenReturn(CLIENT_ID);
        when(props.getRedirectUri()).thenReturn(REDIRECT_URI);
    }

    @Test
    @DisplayName("Should build default auth URL with required parameters")
    void shouldBuildDefaultAuthUrl() {

        String url = googleUrlBuilder.buildDefaultAuthUrl();

        assertTrue(url.contains("client_id=" + CLIENT_ID));
        assertTrue(url.contains("redirect_uri=" + REDIRECT_URI));
        assertTrue(url.contains("response_type=code"));
        assertTrue(url.contains("scope=openid%20email%20profile"));
    }

    @Test
    @DisplayName("Should build full URL with account select and offline access")
    void shouldBuildFullAuthUrlWithAccountSelect() {

        String roles = "USER_ROLE,";

        String url = googleUrlBuilder.buildFullAuthUrlWithAccountSelect(roles);

        assertTrue(url.contains("access_type=offline"));
        assertTrue(url.contains("prompt=select_account"));
        assertTrue(url.contains("state=" + roles));

        assertTrue(url.contains("client_id=" + CLIENT_ID));

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build();
        assertEquals("offline", uriComponents.getQueryParams().getFirst("access_type"));
        assertEquals("select_account", uriComponents.getQueryParams().getFirst("prompt"));
        assertEquals(roles, uriComponents.getQueryParams().getFirst("state"));
    }
}