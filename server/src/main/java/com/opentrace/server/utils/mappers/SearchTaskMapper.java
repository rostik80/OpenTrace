package com.opentrace.server.utils.mappers;

import com.opentrace.server.models.dto.SearchTaskDto;
import com.opentrace.server.models.entities.SearchTaskEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchTaskMapper {

    SearchTaskDto toDto(SearchTaskEntity entity);
    SearchTaskEntity toEntity(SearchTaskDto dto);
}