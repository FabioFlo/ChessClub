package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.model.event.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDto eventToEventDto(EventEntity event);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "available", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventEntity createEventDtoToEvent(CreateEventDto createEventDto);

    List<EventDto> eventEntityListToEventDtoList(List<EventEntity> event);

    @Mapping(target = "available", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EventEntity updateEventDtoToEvent(UpdateEventDto updateEventDto);

    default Page<EventDto> pageEventEntityToPageEventDto(Page<EventEntity> events) {
        return events.map(this::eventToEventDto);
    }
}
