package org.csc.chessclub.service.game;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
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

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final TournamentRepository tournamentRepository;

    @Override
    public GameDto create(CreateGameDto gameDto) {

        if (gameDto.tournamentId() != null && !tournamentRepository.existsById(gameDto.tournamentId())) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(gameDto.tournamentId()));
        }

        GameEntity gameEntity = gameMapper.createGameDtoToGame(gameDto);
        gameEntity.setWhitePlayerName(setEmptyPlayerNameToNN(gameEntity.getWhitePlayerName()));
        gameEntity.setBlackPlayerName(setEmptyPlayerNameToNN(gameEntity.getBlackPlayerName()));
        gameEntity.setAvailable(true);

        return gameMapper.gameToGameDto(gameRepository.save(gameEntity));
    }

    @Override
    public GameEntity update(UpdateGameDto gameDto) {
        UUID tournamentUuid = gameDto.tournamentId();
        if (tournamentUuid != null && !tournamentRepository.existsById(tournamentUuid)) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournamentUuid));
        }

        GameEntity gameEntity = gameRepository.findById(gameDto.uuid())
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(gameDto.uuid())));
        gameMapper.updateGameDtoToGame(gameDto, gameEntity);

        gameEntity.setWhitePlayerName(setEmptyPlayerNameToNN(gameEntity.getWhitePlayerName()));
        gameEntity.setBlackPlayerName(setEmptyPlayerNameToNN(gameEntity.getBlackPlayerName()));

        return gameRepository.save(gameEntity);
    }

    @Override
    public GameDto getByUuid(UUID uuid) {
        return gameMapper.gameToGameDto(gameRepository.findById(uuid)
                .orElseThrow(()
                        -> new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(uuid))));
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        int result = gameRepository.setAvailableFalse(uuid);

        if (result == 0) {
            throw new CustomNotFoundException(NotFoundMessage.GAME_WITH_UUID.format(uuid));
        }
    }

    @Override
    public Page<GameDto> getAll(Pageable pageable) {
        return gameMapper.pageGameEntityToPageGameDto(gameRepository.findAll(pageable));
    }

    @Override
    public Page<GameDto> getAllAvailable(Pageable pageable) {
        return gameMapper.pageGameEntityToPageGameDto(gameRepository.findAllByAvailableTrue(pageable));
    }

    @Override
    public Page<GameEntity> getAllByPlayerName(String playerName, Pageable pageable) {
        return gameRepository.findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(playerName, playerName, pageable);
    }

    @Override
    public Page<GameDto> getAllGamesByWhitePlayerName(String playerName, Pageable pageable) {
        return gameMapper.pageGameEntityToPageGameDto(gameRepository.findByAvailableTrueAndWhitePlayerNameIs(playerName, pageable));
    }

    @Override
    public Page<GameDto> getAllGamesByBlackPlayerName(String playerName, Pageable pageable) {
        return gameMapper.pageGameEntityToPageGameDto(gameRepository.findByAvailableTrueAndBlackPlayerNameIs(playerName, pageable));
    }

    private String setEmptyPlayerNameToNN(String playerName) {
        if (playerName.trim().isEmpty()) {
            return "NN";
        }
        return playerName;
    }
}
