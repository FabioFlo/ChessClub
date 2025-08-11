package org.csc.chessclub.service.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.mapper.GameMapper;
import org.csc.chessclub.model.game.GameEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class GameServiceUnitTests {

  @Spy private GameMapper gameMapper = Mappers.getMapper(GameMapper.class);
  @InjectMocks private GameServiceImpl gameService;
  @Mock private GameRepository gameRepository;

  private Pageable pageable;

  private GameEntity game;
  private GameEntity game2;
  private final String blackPlayer = "OwlMight";
  private final String whitePlayer = "iamsogarbagegod";
  private final Result result = Result.BlackWon;
  private String pgn;
  private UUID uuid;
  private TournamentEntity tournament;

  @BeforeEach
  public void setup() {
    pageable = PageRequest.of(0, 10);
    tournament = TournamentEntity.builder().uuid(UUID.randomUUID()).build();
    uuid = UUID.randomUUID();
    pgn =
        """
                [Event "Live Chess"]
                [Site "Chess.com"]
                [Date "2025.06.04"]
                [Round "?"]
                [White "iamsogarbagegod"]
                [Black "OwlMight"]
                [Result "0-1"]
                [TimeControl "600"]
                [WhiteElo "1720"]
                [BlackElo "1753"]
                [Termination "OwlMight won by resignation"]
                [ECO "E22"]
                [EndTime "14:35:44 GMT+0000"]
                [Link "https://www.chess.com/game/live/139207833194?move=0"]

                1. d4 Nf6 2. c4 e6 3. Nc3 Bb4 4. Qb3 a5 5. a3 Be7 6. e4 a4 7. Nxa4 Nxe4 8. Nc3
                Nxc3 9. Qxc3 d5 10. Nf3 O-O 11. Bd3 h6 12. O-O c5 13. Be3 Nc6 14. cxd5 exd5 15.
                dxc5 Bf6 16. Qd2 Bg4 17. Be2 Re8 18. Rfe1 Na5 19. Bd4 Nb3 20. Bxf6 Qxf6 21. Qxd5
                Nxa1 22. Rxa1 Rxe2 23. Qxb7 Bxf3 24. gxf3 Qg5+ 25. Kf1 Rae8 26. c6 Qd2 27. Kg2
                Rxf2+ 28. Kg3 Rg2+ 29. Kh3 0-1""";
    game =
        GameEntity.builder()
            .uuid(uuid)
            .pgn(pgn)
            .whitePlayerName(whitePlayer)
            .blackPlayerName(blackPlayer)
            .result(result)
            .uuid(uuid)
            .build();

    game2 =
        GameEntity.builder()
            .uuid(UUID.randomUUID())
            .pgn("")
            .result(Result.BlackWon)
            .blackPlayerName(blackPlayer)
            .whitePlayerName(whitePlayer)
            .build();
  }

  @Test
  @DisplayName("Should Create game")
  public void testCreateGame_whenGameEntityProvided_returnGame() {
    CreateGameDto gameDto = new CreateGameDto(whitePlayer, blackPlayer, pgn, result, null);

    when(gameRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

    GameDto createdGame = gameService.create(gameDto);

    assertNotNull(createdGame, "Game should not be null");
    assertNotNull(game.getUuid(), "Uuid should not be null");
    verify(gameRepository, Mockito.times(1)).save(Mockito.any());
  }

  @Test
  @DisplayName("Should update game")
  public void testUpdateGame_whenGameEntityProvided_returnGame() {
    String newPlayerName = "newPlayerName";
    when(gameRepository.save(any(GameEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(gameRepository.findById(uuid)).thenReturn(Optional.ofNullable(game));

    UpdateGameDto gameDto = new UpdateGameDto(uuid, whitePlayer, newPlayerName, "", result, null);

    doAnswer(
            inv -> {
              GameEntity entity = inv.getArgument(1);
              entity.setBlackPlayerName(gameDto.blackPlayerName());
              return null;
            })
        .when(gameMapper)
        .updateGameDtoToGame(gameDto, game);

    GameDto updatedGame = gameService.update(gameDto);

    assertNotNull(updatedGame, "Game should not be null");
    assertEquals(newPlayerName, updatedGame.blackPlayerName(), "Black player name should match");
    assertNotNull(game.getUuid(), "Uuid should not be null");

    verify(gameRepository, Mockito.times(1)).save(Mockito.any());
  }

  @Test
  @DisplayName("Should return the game with the correct uuid")
  public void testGetGame_whenGameFoundByUuid_thenReturnGame() {
    when(gameRepository.findById(game.getUuid())).thenReturn(Optional.of(game));

    GameDto retrievedGame = gameService.getByUuid(game.getUuid());

    assertNotNull(retrievedGame, "Game should not be null");
    assertEquals(game.getUuid(), retrievedGame.uuid(), "Game should match");
    verify(gameRepository, Mockito.times(1)).findById(game.getUuid());
  }

  @Test
  @DisplayName("Should delete by setting available false")
  public void testDeleteGame_whenGameUuidProvided_shouldSetAvailableFalse() {
    when(gameRepository.setAvailableFalse(game.getUuid())).thenReturn(1);

    assertDoesNotThrow(() -> gameService.delete(game.getUuid()));
  }

  @Test
  @DisplayName("Empty player name set to NN")
  public void
      testCreateGame_whenGameWithEmptyWhiteOrBlackPlayerNameProvided_thenPlayerNameShouldBeSetToNN() {
    String blankPlayerName = "";
    CreateGameDto gameDto = new CreateGameDto(blankPlayerName, blackPlayer, pgn, result, null);

    when(gameRepository.save(any(GameEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    GameDto createdGame = gameService.create(gameDto);

    assertEquals("NN", createdGame.whitePlayerName(), "White player name should be NN");
    assertEquals(
        game.getBlackPlayerName(), createdGame.blackPlayerName(), "Black player name should match");
  }

  @Test
  @DisplayName("Update game - empty player name set to NN")
  public void
      testUpdateGame_whenGameWithEmptyWhiteOrBlackPlayerNameProvided_thenPlayerNameShouldBeSetToNN() {
    String blankPlayerName = "";
    when(gameRepository.save(any(GameEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(gameRepository.findById(uuid)).thenReturn(Optional.ofNullable(game));

    UpdateGameDto gameDto =
        new UpdateGameDto(uuid, whitePlayer, blankPlayerName, pgn, result, null);

    GameDto updatedGame = gameService.update(gameDto);

    assertEquals("NN", updatedGame.blackPlayerName(), "White player name should be NN");
    assertEquals(
        game.getWhitePlayerName(), updatedGame.whitePlayerName(), "Black player name should match");
  }

  @Test
  @DisplayName("Get all paged games")
  public void testGetAllGames_whenPageAndSizeProvided_returnPagedGames() {
    List<GameEntity> allGames = List.of(game);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findAll(any(Pageable.class))).thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAll(pageable);

    assertAll(
        "Page assertions",
        () -> assertEquals(1, result.getTotalElements()),
        () -> assertEquals(0, result.getNumber()),
        () -> assertEquals(10, result.getSize()),
        () -> assertEquals(game.getUuid(), result.getContent().getFirst().uuid()));
  }

  @Test
  @DisplayName("Get all paged games available")
  public void testGetAllGamesAvailable_whenPageAndSizeProvided_returnPagedGames() {
    game.setAvailable(true);
    List<GameEntity> allGames = List.of(game);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findAllByAvailableTrue(any(Pageable.class))).thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAllAvailable(pageable);

    assertAll(
        "Page assertions",
        () -> assertEquals(1, result.getTotalElements()),
        () -> assertEquals(0, result.getNumber()),
        () -> assertEquals(10, result.getSize()),
        () -> assertEquals(game.getUuid(), result.getContent().getFirst().uuid()));
  }

  @Test
  @DisplayName("Get paged games by player name")
  public void testGetAllGames_whenPageSizeAndPlayerNameProvided_returnPagedGames() {
    String playerName = blackPlayer;
    List<GameEntity> allGames = List.of(game, game2);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(
            playerName, playerName, pageable))
        .thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAllByPlayerName(playerName, pageable);

    assertAll(
        "Paged games by player name assertions",
        () -> assertEquals(2, result.getTotalElements(), "Should return two games"),
        () ->
            assertEquals(
                playerName,
                result.getContent().getFirst().blackPlayerName(),
                "Black player name should be " + playerName));
  }

  @Test
  @DisplayName("Get games where player is white player")
  public void testGetAllGames_whenPlayerNameProvided_returnGamesWhereIsTheWhitePlayer() {
    List<GameEntity> allGames = List.of(game, game2);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findByAvailableTrueAndWhitePlayerNameIs(whitePlayer, pageable))
        .thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAllGamesByWhitePlayerName(whitePlayer, pageable);

    assertAll(
        "Games where player plays white assertions",
        () -> assertEquals(2, result.getTotalElements(), "Should return two games"),
        () ->
            assertEquals(
                whitePlayer,
                result.getContent().getFirst().whitePlayerName(),
                "White player name should be " + whitePlayer));
  }

  @Test
  @DisplayName("Get games where player is black player")
  public void testGetAllGames_whenPlayerNameProvided_returnGamesWhereIsTheBlackPlayer() {
    List<GameEntity> allGames = List.of(game, game2);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findByAvailableTrueAndBlackPlayerNameIs(blackPlayer, pageable))
        .thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAllGamesByBlackPlayerName(blackPlayer, pageable);

    assertAll(
        "Games where player plays white assertions",
        () -> assertEquals(2, result.getTotalElements(), "Should return two games"),
        () ->
            assertEquals(
                blackPlayer,
                result.getContent().getFirst().blackPlayerName(),
                "White player name should be " + blackPlayer));
  }

  @Test
  @DisplayName("Get all paged games by tournament uuid")
  public void
      testGetAllGamesAvailableByTournamentUuid_whenPageableAndTournamentUuidProvided_returnPagedGames() {
    game.setAvailable(true);
    List<GameEntity> allGames = List.of(game);
    Page<GameEntity> pagedGames = new PageImpl<>(allGames, pageable, allGames.size());

    when(gameRepository.findGameEntitiesByTournament_UuidAndAvailableTrue(
            tournament.getUuid(), pageable))
        .thenReturn(pagedGames);

    Page<GameDto> result = gameService.getAllByTournamentUuid(tournament.getUuid(), pageable);

    assertAll(
        "Page assertions",
        () -> assertEquals(1, result.getTotalElements()),
        () -> assertEquals(0, result.getNumber()),
        () -> assertEquals(10, result.getSize()),
        () -> assertEquals(game.getUuid(), result.getContent().getFirst().uuid()));
  }
}
