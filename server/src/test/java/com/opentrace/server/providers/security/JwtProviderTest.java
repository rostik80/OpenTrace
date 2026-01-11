package com.opentrace.server.providers.security;

import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.models.entities.UserEntity;
import com.opentrace.server.properties.JwtProperties;
import com.opentrace.server.utils.builders.JwtTokenBuilder;
import com.opentrace.server.utils.mappers.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private JwtTokenBuilder tokenBuilder;

    @InjectMocks
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Should generate a temporary key when secret is missing")
    void shouldGenerateTemporaryKeyWhenSecretIsMissing() {

        when(jwtProperties.getSecret()).thenReturn(null);

        jwtProvider.afterPropertiesSet();

        assertNotNull(jwtProvider.getKey());
        assertEquals("HmacSHA256", jwtProvider.getKey().getAlgorithm());
    }

    @Test
    @DisplayName("Should use provided secret from properties when available")
    void shouldUseSecretFromProperties() {

        String mySecret = "12345678901234567890123456789012";
        when(jwtProperties.getSecret()).thenReturn(mySecret);

        jwtProvider.afterPropertiesSet();

        assertNotNull(jwtProvider.getKey());
        assertEquals("HmacSHA256", jwtProvider.getKey().getAlgorithm());
    }

    @Test
    @DisplayName("Should coordinate token creation between mapper and builder")
    void shouldCreateTokenSuccessfully() {

        UserDto dto = new UserDto();
        UserEntity entity = new UserEntity();
        List<String> roles = List.of("USER");
        String expectedToken = "header.payload.signature";

        when(jwtProperties.getSecret()).thenReturn("secret-key-long-enough-for-testing-purposes");
        jwtProvider.afterPropertiesSet();

        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(tokenBuilder.buildToken(entity, roles, jwtProvider.getKey())).thenReturn(expectedToken);

        String token = jwtProvider.createToken(dto, roles);

        assertEquals(expectedToken, token);
        verify(userMapper).toEntity(dto);
        verify(tokenBuilder).buildToken(entity, roles, jwtProvider.getKey());
    }
}