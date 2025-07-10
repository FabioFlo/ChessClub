package org.csc.chessclub.service.game;

import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.model.game.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GameService {
    GameDto create(CreateGameDto gameDto);

    GameEntity update(UpdateGameDto gameDto);

    GameDto getByUuid(UUID uuid);

    GameEntity delete(UUID uuid);

    Page<GameEntity> getAll(Pageable pageable);

    Page<GameEntity> getAllAvailable(Pageable pageable);

    Page<GameEntity> getAllByPlayerName(String playerName, Pageable pageable);

    Page<GameEntity> getAllGamesByWhitePlayerName(String playerName, Pageable pageable);

    Page<GameEntity> getAllGamesByBlackPlayerName(String playerName, Pageable pageable);
}
