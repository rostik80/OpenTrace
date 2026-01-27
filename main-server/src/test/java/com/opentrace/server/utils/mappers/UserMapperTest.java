package com.opentrace.server.utils.mappers;

import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.models.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Should map GoogleUserDTO to UserEntity correctly with custom mappings")
    void shouldMapGoogleUserDtoToUserEntity() {

        GoogleUserDto googleDto = new GoogleUserDto();
        googleDto.setSub("google-12345");
        googleDto.setPicture("https://avatar.com/photo.jpg");
        googleDto.setName("Test User");
        googleDto.setEmail("test@gmail.com");

        UserEntity entity = userMapper.toEntity(googleDto);

        assertNotNull(entity);
        assertNull(entity.getId(), "ID should be ignored");
        assertNull(entity.getStatus(), "Status should be ignored");

        assertEquals(googleDto.getSub(), entity.getGoogleSub(), "Sub should map to googleSub");
        assertEquals(googleDto.getPicture(), entity.getAvatarUrl(), "Picture should map to avatarUrl");
        assertEquals(googleDto.getName(), entity.getName());
        assertEquals(googleDto.getEmail(), entity.getEmail());
    }

    @Test
    @DisplayName("Should map UserEntity to UserDTO")
    void shouldMapEntityToDto() {

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("Java Giant");
        entity.setGoogleSub("sub-999");

        UserDto dto = userMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getGoogleSub(), dto.getGoogleSub());
    }
}