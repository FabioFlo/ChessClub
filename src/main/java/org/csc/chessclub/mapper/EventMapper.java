package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.model.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    GetEventDto eventToGetEventDto(EventEntity event);

    EventEntity createEventDtoToEvent(CreateEventDto createEventDto);
}
