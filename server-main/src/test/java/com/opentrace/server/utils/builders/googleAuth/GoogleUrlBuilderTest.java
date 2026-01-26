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

import java.util.Base64;

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
    @DisplayName("Should build full URL with correctly encoded state containing roles and publicKey")
    void shouldBuildFullAuthUrlWithEncodedState() {
        String roles = "REQUESTER,WORKER";
        String publicKey = "test-public-key";
        String expectedState = Base64.getUrlEncoder().withoutPadding()
                .encodeToString((roles + "|" + publicKey).getBytes());

        String url = googleUrlBuilder.buildFullAuthUrlWithAccountSelect(roles, publicKey);

        UriComponents uri = UriComponentsBuilder.fromUriString(url).build();

        assertEquals("https", uri.getScheme());
        assertEquals("accounts.google.com", uri.getHost());
        assertEquals("/o/oauth2/v2/auth", uri.getPath());

        assertEquals(CLIENT_ID, uri.getQueryParams().getFirst("client_id"));
        assertEquals(REDIRECT_URI, uri.getQueryParams().getFirst("redirect_uri"));
        assertEquals("offline", uri.getQueryParams().getFirst("access_type"));
        assertEquals("select_account", uri.getQueryParams().getFirst("prompt"));
        assertEquals("code", uri.getQueryParams().getFirst("response_type"));
        assertEquals("openid email profile", uri.getQueryParams().getFirst("scope"));
        assertEquals(expectedState, uri.getQueryParams().getFirst("state"));
    }

    @Test
    @DisplayName("Should contain all required base OAuth2 parameters")
    void shouldContainBaseParameters() {
        String url = googleUrlBuilder.buildFullAuthUrlWithAccountSelect("any", "any");

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).build();
        assertEquals(CLIENT_ID, uriComponents.getQueryParams().getFirst("client_id"));
        assertEquals(REDIRECT_URI, uriComponents.getQueryParams().getFirst("redirect_uri"));
        assertEquals("code", uriComponents.getQueryParams().getFirst("response_type"));
        assertEquals("openid email profile", uriComponents.getQueryParams().getFirst("scope"));
    }
}