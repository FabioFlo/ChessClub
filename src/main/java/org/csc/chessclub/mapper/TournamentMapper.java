package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.model.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    TournamentDto tournamentToTournamentDto(TournamentEntity tournament);

    @Mapping(target = "event", source = "eventId")
    TournamentEntity createTournamentDtoToTournamentEntity(CreateTournamentDto tournamentDto);

    default EventEntity map(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return EventEntity.builder().uuid(eventId).build();
    }
}
