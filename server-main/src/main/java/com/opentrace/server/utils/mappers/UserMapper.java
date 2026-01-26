package com.opentrace.server.utils.mappers;

import com.opentrace.server.models.dto.UserDto;
import com.opentrace.server.models.dto.GoogleUserDto;
import com.opentrace.server.models.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "googleSub", source = "sub")
    @Mapping(target = "avatarUrl", source = "picture")
    @Mapping(target = "status", ignore = true)
    UserEntity toEntity(GoogleUserDto googleUserDto);

    UserEntity toEntity(UserDto userDto);

    UserEntity clone(UserEntity entity);
}