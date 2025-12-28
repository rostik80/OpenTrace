package com.opentrace.server.mappers;

import com.opentrace.server.models.dto.UserDTO;
import com.opentrace.server.models.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(UserEntity entity);
    UserEntity toEntity(UserDTO dto);
}