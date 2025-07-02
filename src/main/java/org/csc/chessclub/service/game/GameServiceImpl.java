package org.csc.chessclub.service.game;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.mapper.GameMapper;
import org.csc.chessclub.model.game.GameEntity;
import org.csc.chessclub.repository.GameRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final TournamentRepository tournamentRepository;

    @Override
    public GameEntity create(GameEntity gameEntity) {
        UUID tournamentUuid = gameEntity.getTournament().getUuid();
        if (tournamentUuid != null && !tournamentRepository.existsById(tournamentUuid)) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournamentUuid));
        }
        gameEntity.setWhitePlayerName(setEmptyPlayerNameToNN(gameEntity.getWhitePlayerName()));
        gameEntity.setBlackPlayerName(setEmptyPlayerNameToNN(gameEntity.getBlackPlayerName()));
        gameEntity.setAvailable(true);

        return gameRepository.save(gameEntity);
    }

    @Override
    public GameEntity update(UpdateGameDto gameDto) {
        GameEntity gameEntity = gameRepository.findById(gameDto.uuid())
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(gameDto.uuid())));

        UUID tournamentUuid = gameDto.tournamentId();
        if (tournamentUuid != null && !tournamentRepository.existsById(tournamentUuid)) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournamentUuid));
        }
        gameMapper.updateGameDtoToGame(gameDto, gameEntity);

        gameEntity.setWhitePlayerName(setEmptyPlayerNameToNN(gameEntity.getWhitePlayerName()));
        gameEntity.setBlackPlayerName(setEmptyPlayerNameToNN(gameEntity.getBlackPlayerName()));

        return gameRepository.save(gameEntity);
    }

    @Override
    public GameEntity getByUuid(UUID uuid) {
        return gameRepository.findById(uuid)
                .orElseThrow(()
                        -> new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(uuid)));
    }

    @Override
    public GameEntity delete(UUID uuid) {
        Optional<GameEntity> game = gameRepository.findById(uuid);
        if (game.isEmpty()) {
            throw new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(uuid));
        }
        game.get().setAvailable(false);
        return gameRepository.save(game.get());
    }

    @Override
    public Page<GameEntity> getAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    @Override
    public Page<GameEntity> getAllAvailable(Pageable pageable) {
        return gameRepository.findAllByAvailableTrue(pageable);
    }

    @Override
    public Page<GameEntity> getAllByPlayerName(String playerName, Pageable pageable) {
        return gameRepository.findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(playerName, playerName, pageable);
    }

    @Override
    public Page<GameEntity> getAllGamesByWhitePlayerName(String playerName, Pageable pageable) {
        return gameRepository.findByAvailableTrueAndWhitePlayerNameIs(playerName, pageable);
    }

    @Override
    public Page<GameEntity> getAllGamesByBlackPlayerName(String playerName, Pageable pageable) {
        return gameRepository.findByAvailableTrueAndBlackPlayerNameIs(playerName, pageable);
    }

    private String setEmptyPlayerNameToNN(String playerName) {
        if (playerName.trim().isEmpty()) {
            return "NN";
        }
        return playerName;
    }
}
