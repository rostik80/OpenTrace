package com.opentrace.server.utils.mappers;

import com.opentrace.shared.models.api.response.SearchStatusResponse;
import com.opentrace.shared.models.dto.SearchDto;
import com.opentrace.server.models.entities.SearchEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = SearchTaskMapper.class)
public interface SearchMapper {

    SearchDto toDto(SearchEntity entity);
    SearchEntity toEntity(SearchDto dto);

    SearchStatusResponse toStatusResponse(SearchEntity entity);
}