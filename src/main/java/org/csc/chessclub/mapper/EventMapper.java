package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.model.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto eventToGetEventDto(EventEntity event);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "available", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventEntity createEventDtoToEvent(CreateEventDto createEventDto);

    List<EventDto> eventEntityListToGetEventDtoList(List<EventEntity> event);

    @Mapping(target = "available", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventEntity eventDetailsDtoToEvent(UpdateEventDto updateEventDto);
}
