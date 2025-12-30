package com.opentrace.server.mappers;

import com.opentrace.server.models.dto.RoleDTO;
import com.opentrace.server.models.entities.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "userId", source = "user.id")
    RoleDTO toDTO(RoleEntity roleEntity);

    @Mapping(target = "user.id", source = "userId")
    RoleEntity toEntity(RoleDTO roleDTO);
}