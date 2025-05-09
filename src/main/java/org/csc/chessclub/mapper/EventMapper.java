package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.model.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    GetEventDto eventToGetEventDto(EventEntity event);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "available", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventEntity createEventDtoToEvent(CreateEventDto createEventDto);

    List<GetEventDto> eventEntityListToGetEventDtoList(List<EventEntity> event);
}
