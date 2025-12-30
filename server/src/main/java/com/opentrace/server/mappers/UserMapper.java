package com.opentrace.server.mappers;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.dto.GoogleUserDTO;
import com.opentrace.server.models.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "googleSub", source = "sub")
    @Mapping(target = "avatarUrl", source = "picture")
    @Mapping(target = "status", ignore = true)
    UserEntity toEntity(GoogleUserDTO googleUserDto);

    UserEntity toEntity(UserDTO userDto);
}