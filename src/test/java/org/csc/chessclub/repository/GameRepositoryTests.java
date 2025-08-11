package org.csc.chessclub.repository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.csc.chessclub.controller.BaseTestConfiguration;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.game.GameEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class GameRepositoryTests extends BaseTestConfiguration {

  @Autowired private GameRepository gameRepository;
  private GameEntity gameOne;
  private GameEntity unavailableGame;
  private Pageable pageable;
  @Autowired private TournamentRepository tournamentRepository;
  private TournamentEntity tournament;

  @BeforeAll
  public void setup() {
    pageable = PageRequest.of(0, 10);
    tournament =
        tournamentRepository.save(
            TournamentEntity.builder()
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(true)
                .build());
    gameRepository.deleteAll();
    createBaseTestData();
  }

  @Override
  public void createBaseTestData() {
    String playerOne = "Gino";
    String playerTwo = "Paolo";
    Result whiteWon = Result.WhiteWon;
    Result blackWon = Result.BlackWon;
    String pgn = "";

    gameOne =
        GameEntity.builder()
            .pgn(pgn)
            .whitePlayerName(playerOne)
            .blackPlayerName(playerTwo)
            .result(whiteWon)
            .available(true)
            .tournament(tournament)
            .build();

    GameEntity gameTwo =
        GameEntity.builder()
            .pgn(pgn)
            .whitePlayerName(playerTwo)
            .blackPlayerName(playerOne)
            .result(blackWon)
            .available(true)
            .build();

    GameEntity gameThree =
        GameEntity.builder()
            .pgn(pgn)
            .whitePlayerName(playerTwo)
            .blackPlayerName(playerOne)
            .result(whiteWon)
            .available(true)
            .build();

    GameEntity nnPlayersGame =
        GameEntity.builder()
            .pgn(pgn)
            .whitePlayerName("NN")
            .blackPlayerName("NN")
            .result(whiteWon)
            .available(true)
            .build();

    unavailableGame =
        GameEntity.builder()
            .pgn(pgn)
            .whitePlayerName(playerTwo)
            .blackPlayerName(playerOne)
            .result(whiteWon)
            .available(false)
            .build();

    gameRepository.save(gameOne);
    gameRepository.save(gameTwo);
    gameRepository.save(gameThree);
    gameRepository.save(nnPlayersGame);
    gameRepository.save(unavailableGame);
  }

  @Test
  @DisplayName("Get games - with given player name if available")
  void testFindByPlayerName_whenPlayerNameGiven_returnGamesWithGivenPlayerName() {
    String playerName = "Paolo";
    Page<GameEntity> gamesRetrieved =
        gameRepository.findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(
            playerName, playerName, pageable);

    assertAll(
        "Game search by player name",
        () -> assertNotNull(gamesRetrieved, "The retrieved page should not be null"),
        () ->
            assertTrue(
                gamesRetrieved.getContent().stream().allMatch(GameEntity::isAvailable),
                "All games should have available true"),
        () ->
            assertEquals(
                3,
                gamesRetrieved.getTotalElements(),
                "Should return 3 games with player name " + playerName),
        () ->
            assertFalse(
                gamesRetrieved.getContent().isEmpty(), "The game list should not be empty"));
  }

  @Test
  @DisplayName("Get games - with NN players if available")
  void testFindByPlayerName_whenPlayerNameGivenIsNN_returnGamesWithGivenPlayerName() {
    String playerName = "NN";
    Page<GameEntity> gamesRetrieved =
        gameRepository.findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(
            playerName, playerName, pageable);

    assertAll(
        "Game search by player name",
        () -> assertNotNull(gamesRetrieved, "Game with player name " + playerName + " not found"),
        () ->
            assertTrue(
                gamesRetrieved.stream().allMatch(GameEntity::isAvailable),
                "All game should have available true"),
        () ->
            assertEquals(
                1,
                gamesRetrieved.getTotalElements(),
                "Game with player name " + playerName + " found"),
        () ->
            assertFalse(
                gamesRetrieved.getContent().isEmpty(), "The game list should not be empty"));
  }

  @Test
  @DisplayName("Get games - with playerName equal white player if available")
  void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheWhitePlayerName() {
    String playerName = "Gino";
    unavailableGame.setWhitePlayerName(playerName);

    Page<GameEntity> gamesRetrieved =
        gameRepository.findByAvailableTrueAndWhitePlayerNameIs(playerName, pageable);

    assertAll(
        "Game search by player name",
        () -> assertNotNull(gamesRetrieved, "Games should not be null"),
        () ->
            assertTrue(
                gamesRetrieved.stream().allMatch(GameEntity::isAvailable),
                "All games should be available"),
        () -> assertEquals(1, gamesRetrieved.getTotalElements(), "Should find one game"),
        () ->
            assertEquals(
                gameOne.getUuid(),
                gamesRetrieved.getContent().getFirst().getUuid(),
                "Game one should be equal to first"));
  }

  @Test
  @DisplayName("Get games - with playerName equal black player if available")
  void testFindByPlayerName_whenPlayerNameGiven_shouldReturnAllGamesWhereIsTheBlackPlayerName() {
    String playerName = "Gino";
    unavailableGame.setBlackPlayerName(playerName);
    Page<GameEntity> gamesRetrieved =
        gameRepository.findByAvailableTrueAndBlackPlayerNameIs(playerName, pageable);

    assertAll(
        "Game search by player name",
        () -> assertNotNull(gamesRetrieved, "Games should not be null"),
        () ->
            assertTrue(
                gamesRetrieved.stream().allMatch(GameEntity::isAvailable),
                "All games should be available"),
        () -> assertEquals(2, gamesRetrieved.getTotalElements(), "Should find two games"));
  }

  @Test
  @DisplayName("Find all available games")
  void testGetAllAvailableGames_returnGamesWithAvailableTrue() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<GameEntity> result = gameRepository.findAllByAvailableTrue(pageable);

    boolean allAvailable = result.getContent().stream().allMatch(GameEntity::isAvailable);

    assertAll(
        "Get all available games",
        () -> assertFalse(result.isEmpty(), "Result should not be null"),
        () -> assertTrue(allAvailable, "All games should be available"));
  }

  @Test
  @DisplayName("Correctly set available to false")
  @Transactional
  void testSetAvailableFalse_whenGivenUuid_returnResult() {
    int result = gameRepository.setAvailableFalse(gameOne.getUuid());

    assertEquals(1, result, "Result should be equal to 1");
  }

  @Test
  @DisplayName("Find all available games")
  void testGetAllAvailableGames_whenTournamentUuidAndPageableProvided_returnGames() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<GameEntity> result =
        gameRepository.findGameEntitiesByTournament_UuidAndAvailableTrue(
            tournament.getUuid(), pageable);

    boolean allAvailable = result.getContent().stream().allMatch(GameEntity::isAvailable);

    assertAll(
        "Get all available games",
        () -> assertFalse(result.isEmpty(), "Result should not be null"),
        () -> assertTrue(allAvailable, "All games should be available"),
        () ->
            assertEquals(
                tournament.getUuid(),
                result.getContent().getFirst().getTournament().getUuid(),
                "Tournament uuid should be equal"));
  }
}
