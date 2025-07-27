package org.csc.chessclub.service.game;

import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.repository.GameRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceExceptionTests {

  @InjectMocks
  private GameServiceImpl gameService;
  @Mock
  private GameRepository gameRepository;
  @Mock
  private TournamentRepository tournamentRepository;

  private CreateGameDto createGame;
  private UpdateGameDto updateGame;

  private Result result;
  private UUID uuid;
  private String pgn;
  private String whitePlayer;
  private String blackPlayer;

  @BeforeEach
  public void setup() {
    uuid = UUID.randomUUID();
    pgn = """
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
    blackPlayer = "OwlMight";
    whitePlayer = "iamsogarbagegod";
    result = Result.BlackWon;
  }

  @Test
  @DisplayName("Get by id - throw when Game not found")
  void testGameService_whenGameNotFound_thenThrowNotFoundException() {
    CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
        () -> gameService.getByUuid(uuid));
    assertEquals(NotFoundMessage.GAME_WITH_UUID.format(uuid), exception.getMessage());

    verify(gameRepository, times(1)).findById(uuid);
  }

  @Test
  @DisplayName("Create game - throw id tournament inserted not found")
  void testGameServiceCreate_whenTournamentNotFound_thenThrowNotFoundException() {
    createGame = new CreateGameDto(whitePlayer, blackPlayer, pgn, result, UUID.randomUUID());

    when(tournamentRepository.existsById(createGame.tournamentId())).thenReturn(false);

    CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
        () -> gameService.create(createGame));

    assertEquals(NotFoundMessage.TOURNAMENT_WITH_UUID.format(createGame.tournamentId()),
        exception.getMessage());
  }

  @Test
  @DisplayName("Update game - throw id tournament inserted not found")
  void testGameServiceUpdate_whenTournamentNotFound_thenThrowNotFoundException() {
    updateGame = new UpdateGameDto(uuid, whitePlayer, blackPlayer, pgn, result, UUID.randomUUID());

    when(tournamentRepository.existsById(updateGame.tournamentId())).thenReturn(false);

    CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
        () -> gameService.update(updateGame));

    assertEquals(NotFoundMessage.TOURNAMENT_WITH_UUID.format(updateGame.tournamentId()),
        exception.getMessage());
  }
}
