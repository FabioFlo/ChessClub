package org.csc.chessclub.service.game;

import org.csc.chessclub.model.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GameService {
    GameEntity create(GameEntity gameEntity);

    GameEntity update(GameEntity gameEntity);

    GameEntity getByUuid(UUID uuid);

    GameEntity delete(UUID uuid);

    Page<GameEntity> getAll(Pageable pageable);

    Page<GameEntity> getAllByPlayerName(String playerName, Pageable pageable);
}
