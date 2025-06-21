package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.model.GameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameDto gameToGameDto(GameEntity gameEntity);

    GameEntity createGameDtoToGame(CreateGameDto gameDto);

    GameEntity updateGameDtoToGame(UpdateGameDto gameDto);

    List<GameDto> listOfGamesToGameDto(List<GameEntity> games);

    default Page<GameDto> pageGameEntityToPageGameDto(Page<GameEntity> games) {
        return games.map(this::gameToGameDto);
    }
}
