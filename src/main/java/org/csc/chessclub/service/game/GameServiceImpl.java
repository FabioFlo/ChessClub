package org.csc.chessclub.service.game;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.model.GameEntity;
import org.csc.chessclub.repository.GameRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public GameEntity create(GameEntity gameEntity) {
        gameEntity.setAvailable(true);
        return gameRepository.save(gameEntity);
    }
}
