package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    TournamentDto tournamentToTournamentDto(TournamentEntity tournament);

    @Mapping(target = "event", source = "eventId")
    TournamentEntity updateTournamentToTournament(UpdateTournamentDto updateTournamentDto);

    @Mapping(target = "event", source = "eventId")
    TournamentEntity createTournamentDtoToTournamentEntity(CreateTournamentDto tournamentDto);

    default EventEntity map(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return EventEntity.builder().uuid(eventId).build();
    }

    default Page<TournamentDto> pageTournamentEntityToPageTournamentDto(Page<TournamentEntity> pageOfTournament) {
        return pageOfTournament.map(this::tournamentToTournamentDto);
    }
}
