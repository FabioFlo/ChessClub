package org.csc.chessclub.service.game;

import org.csc.chessclub.model.GameEntity;

import java.util.UUID;

public interface GameService {
    GameEntity create(GameEntity gameEntity);
    GameEntity update(GameEntity gameEntity);

    GameEntity getByUuid(UUID uuid);

    GameEntity delete(UUID uuid);
}
