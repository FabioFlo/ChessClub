package org.csc.chessclub.service.game;

import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.GameEntity;
import org.csc.chessclub.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceUnitTests {

    @InjectMocks
    private GameServiceImpl gameService;
    @Mock
    private GameRepository gameRepository;

    private GameEntity game;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        String pgn = """
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
        String whitePlayer = "iamsogarbagegod";
        String blackPlayer = "OwlMight";
        Result result = Result.BlackWon;
        game = GameEntity.builder()
                .uuid(uuid)
                .pgn(pgn)
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .result(result)
                .build();
    }

    @Test
    @DisplayName("Should Create game")
    public void testCreateGame_whenGameEntityProvided_returnGame() {
        when(gameRepository.save(Mockito.any())).thenReturn(game);

        GameEntity createdGame = gameService.create(game);

        assertNotNull(createdGame, "Game should not be null");
        assertNotNull(game.getUuid(), "Uuid should not be null");
        assertTrue(game.isAvailable(), "Game should be available");
        verify(gameRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Should update game")
    public void testUpdateGame_whenGameEntityProvided_returnGame() {
        String newPlayerName = "newPlayerName";
        when(gameRepository.existsById(game.getUuid())).thenReturn(true);
        when(gameRepository.save(any(GameEntity.class))).thenReturn(game);

        game.setBlackPlayer(newPlayerName);

        GameEntity updatedGame = gameService.update(game);

        assertNotNull(updatedGame, "Game should not be null");
        assertEquals(newPlayerName, updatedGame.getBlackPlayer(), "Black player name should match");
        assertNotNull(game.getUuid(), "Uuid should not be null");

        verify(gameRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    @DisplayName("Should return the game with the right uuid")
    public void testGetGame_whenGameFoundByUuid_thenReturnGame() {
        when(gameRepository.findById(game.getUuid())).thenReturn(Optional.of(game));

        GameEntity retrievedGame = gameService.getByUuid(game.getUuid());

        assertNotNull(retrievedGame, "Game should not be null");
        assertEquals(game, retrievedGame, "Game should match");
        verify(gameRepository, Mockito.times(1)).findById(game.getUuid());

    }
}
