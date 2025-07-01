package org.csc.chessclub.repository;

import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.game.GameEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

public class GameRepositoryTests extends TestContainerConfig {

    @Autowired
    private GameRepository gameRepository;
    private GameEntity gameOne;
    private GameEntity gameTwo;
    private Pageable pageable;

    @BeforeAll
    public void setup() {
        pageable = PageRequest.of(0, 10);
        String playerOne = "Gino";
        String playerTwo = "Paolo";
        Result whiteWon = Result.WhiteWon;
        Result blackWon = Result.BlackWon;
        String pgn = "";

        gameOne = GameEntity.builder()
                .pgn(pgn)
                .whitePlayerName(playerOne)
                .blackPlayerName(playerTwo)
                .result(whiteWon)
                .available(true)
                .build();

        gameTwo = GameEntity.builder()
                .pgn(pgn)
                .whitePlayerName(playerTwo)
                .blackPlayerName(playerOne)
                .result(blackWon)
                .available(true)
                .build();

        GameEntity gameThree = GameEntity.builder()
                .pgn(pgn)
                .whitePlayerName(playerTwo)
                .blackPlayerName(playerOne)
                .result(whiteWon)
                .available(true)
                .build();

        GameEntity nnPlayersGame = GameEntity.builder()
                .pgn(pgn)
                .whitePlayerName("NN")
                .blackPlayerName("NN")
                .result(whiteWon)
                .available(true)
                .build();

        gameRepository.save(gameOne);
        gameRepository.save(gameTwo);
        gameRepository.save(gameThree);
        gameRepository.save(nnPlayersGame);
    }
    //TODO: result should have available true
    @Test
    @DisplayName("Should return games with the given player name")
    void testFindByPlayerName_whenPlayerNameGiven_returnGamesWithGivenPlayerName() {
        String playerName = "Paolo";
        Page<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByWhitePlayerNameOrBlackPlayerName(
                playerName, playerName, pageable);

        assertAll("Game search by player name",
                () -> assertNotNull(gamesRetrieved, "The retrieved page should not be null"),
                () -> assertEquals(3, gamesRetrieved.getTotalElements(), "Should return 3 games with player name " + playerName),
                () -> assertFalse(gamesRetrieved.getContent().isEmpty(), "The game list should not be empty"),
                () -> assertEquals(gameOne, gamesRetrieved.getContent().getFirst(), "First game should match expected gameOne")
        );
    }

    @Test
    @DisplayName("Should return games with NN players")
    void testFindByPlayerName_whenPlayerNameGivenIsNN_returnGamesWithGivenPlayerName() {
        String playerName = "NN";
        Page<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByWhitePlayerNameOrBlackPlayerName(
                playerName, playerName, pageable);

        assertAll("Game search by player name",
                () -> assertNotNull(gamesRetrieved, "Game with player name " + playerName + " not found"),
                () -> assertEquals(1, gamesRetrieved.getTotalElements(), "Game with player name " + playerName + " found"),
                () -> assertFalse(gamesRetrieved.getContent().isEmpty(), "The game list should not be empty")
        );
    }

    @Test
    @DisplayName("Find games where playerName is the white player")
    void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheWhitePlayerName() {
        String playerName = "Gino";
        Page<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByWhitePlayerName(playerName, pageable);

        assertAll("Game search by player name",
                () -> assertNotNull(gamesRetrieved, "Games should not be null"),
                () -> assertEquals(1, gamesRetrieved.getTotalElements(), "Should find one game"),
                () -> assertEquals(gameOne, gamesRetrieved.getContent().getFirst(), "Game one should be equal to first"));

    }

    @Test
    @DisplayName("Find games where playerName is the black player")
    void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheBlackPlayerName() {
        String playerName = "Gino";
        Page<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByBlackPlayerName(playerName, pageable);

        assertAll("Game search by player name",
                () -> assertNotNull(gamesRetrieved, "Games should not be null"),
                () -> assertEquals(2, gamesRetrieved.getTotalElements(), "Should find two games"),
                () -> assertEquals(gameTwo, gamesRetrieved.getContent().getFirst(), "Game one should be equal to first"));
    }

    //TODO: return only available entities (this should be default for user)
}
