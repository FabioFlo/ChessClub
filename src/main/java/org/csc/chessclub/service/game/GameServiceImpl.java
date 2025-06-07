package org.csc.chessclub.service.game;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.GameServiceException;
import org.csc.chessclub.model.GameEntity;
import org.csc.chessclub.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public GameEntity create(GameEntity gameEntity) {
        gameEntity.setAvailable(true);
        return gameRepository.save(gameEntity);
    }

    @Override
    public GameEntity update(GameEntity gameEntity) {
        if (!gameRepository.existsById(gameEntity.getUuid())) {
            throw new GameServiceException(NotFoundMessage.GAME_WITH_UUID.format(gameEntity.getUuid()));
        }
        return gameRepository.save(gameEntity);
    }

    @Override
    public GameEntity getByUuid(UUID uuid) {
        return gameRepository.findById(uuid)
                .orElseThrow(()
                        -> new GameServiceException(NotFoundMessage.GAME_WITH_UUID.format(uuid)));
    }
}
