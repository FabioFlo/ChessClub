package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.model.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

TournamentDto tournamentToTournamentDto(TournamentEntity tournament);
}
