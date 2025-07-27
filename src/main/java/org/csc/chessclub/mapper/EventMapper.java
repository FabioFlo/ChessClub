package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.model.event.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

  EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

  EventDto eventToEventDto(EventEntity event);

  EventEntity createEventDtoToEvent(CreateEventDto createEventDto);

  List<EventDto> eventEntityListToEventDtoList(List<EventEntity> event);

  EventEntity updateEventDtoToEvent(UpdateEventDto updateEventDto,
      @MappingTarget EventEntity eventEntity);

  default Page<EventDto> pageEventEntityToPageEventDto(Page<EventEntity> events) {
    return events.map(this::eventToEventDto);
  }
}
