package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.model.game.GameEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameDto gameToGameDto(GameEntity gameEntity);

    @Mapping(target = "tournament", source = "tournamentId")
    GameEntity createGameDtoToGame(CreateGameDto gameDto);

    @Mapping(target = "tournament", source = "tournamentId")
    GameEntity updateGameDtoToGame(UpdateGameDto gameDto, @MappingTarget GameEntity entity);

    List<GameDto> listOfGamesToGameDto(List<GameEntity> games);

    default Page<GameDto> pageGameEntityToPageGameDto(Page<GameEntity> games) {
        return games.map(this::gameToGameDto);
    }

    default TournamentEntity map(UUID tournamentId) {
        if (tournamentId == null) {
            return null;
        }
        return TournamentEntity.builder().uuid(tournamentId).build();
    }
}
