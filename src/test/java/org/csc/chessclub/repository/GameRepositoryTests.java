package org.csc.chessclub.repository;

import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.GameEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameRepositoryTests extends TestContainerConfig {

    @Autowired
    private GameRepository gameRepository;
    private GameEntity gameOne;
    private GameEntity gameTwo;

    @BeforeAll
    public void setup() {
        String playerOne = "Gino";
        String playerTwo = "Paolo";
        Result whiteWon = Result.WhiteWon;
        Result blackWon = Result.BlackWon;
        String pgn = "";

        gameOne = GameEntity.builder()
                .pgn(pgn)
                .whitePlayer(playerOne)
                .blackPlayer(playerTwo)
                .result(whiteWon)
                .available(true)
                .build();

        gameTwo = GameEntity.builder()
                .pgn(pgn)
                .whitePlayer(playerTwo)
                .blackPlayer(playerOne)
                .result(blackWon)
                .available(true)
                .build();

        GameEntity gameThree = GameEntity.builder()
                .pgn(pgn)
                .whitePlayer(playerTwo)
                .blackPlayer(playerOne)
                .result(whiteWon)
                .available(true)
                .build();

        gameRepository.save(gameOne);
        gameRepository.save(gameTwo);
        gameRepository.save(gameThree);
    }

    @Test
    @DisplayName("Should return games with the given player name")
    void testFindByPlayerName_whenPlayerNameGiven_returnGamesWithGivenPlayerName() {
        String playerName = "Paolo";
        List<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByWhitePlayerOrBlackPlayer(playerName, playerName);

        assertNotNull(gamesRetrieved,
                "Game with player name " + playerName + " not found");
        assertEquals(2, gamesRetrieved.size(),
                "Game with player name " + playerName + " found");
        assertEquals(gameOne, gamesRetrieved.getFirst(),
                "Game one should be equal to first");
    }

    @Test
    @DisplayName("Find games where playerName is the white player")
    void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheWhitePLayer() {
        String playerName = "Gino";

        List<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByWhitePlayer(playerName);

        assertNotNull(gamesRetrieved, "Games should not be null");
        assertEquals(1, gamesRetrieved.size(),
                "Should find one game");
        assertEquals(gameOne, gamesRetrieved.getFirst(),
                "Game one should be equal to first");
    }

    @Test
    @DisplayName("Find games where playerName is the black player")
    void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheBlackPLayer() {
        String playerName = "Gino";

        List<GameEntity> gamesRetrieved = gameRepository.findGameEntitiesByBlackPlayer(playerName);

        assertNotNull(gamesRetrieved, "Games should not be null");
        assertEquals(2, gamesRetrieved.size(),
                "Should find two games");
        assertEquals(gameTwo, gamesRetrieved.getFirst(),
                "Game one should be equal to first");
    }

}
