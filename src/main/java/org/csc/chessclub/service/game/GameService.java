package org.csc.chessclub.service.game;

import org.csc.chessclub.model.GameEntity;

public interface GameService {
    GameEntity create(GameEntity gameEntity);
    GameEntity update(GameEntity gameEntity);
}
