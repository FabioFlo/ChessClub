package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.game.GameEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapperTests {

    private final GameMapper gameMapper = GameMapper.INSTANCE;

    private GameEntity game;
    private final UUID gameId = UUID.randomUUID();
    private final String whitePlayerName = "White";
    private final String blackPlayerName = "Black";
    private final Result result = Result.BlackWon;
    private final String pgn = """
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

    @BeforeEach
    public void setup() {
        game = GameEntity.builder()
                .uuid(gameId)
                .pgn(pgn)
                .whitePlayerName(whitePlayerName)
                .blackPlayerName(blackPlayerName)
                .result(result)
                .build();
    }

    @Test
    @DisplayName("Correctly map GameEntity to GameDto")
    void shouldMapGameEntityToGameDto() {
        GameDto gameDto = gameMapper.gameToGameDto(game);

        assertEquals(gameDto.uuid(), game.getUuid(),
                "Uuid should match");
        assertEquals(gameDto.blackPlayerName(), game.getBlackPlayerName(),
                "Black player name should match");
        assertEquals(gameDto.whitePlayerName(), game.getWhitePlayerName(),
                "White player name should match");
        assertEquals(gameDto.result(), game.getResult(),
                "Result should match");
        assertEquals(gameDto.pgn(), game.getPgn(),
                "Pgn should match");
    }

    @Test
    @DisplayName("Correctly map CreateGameDto to GameEntity")
    void shouldMapCreateGameDtoToGameEntity() {
        CreateGameDto gameDto = new CreateGameDto(whitePlayerName, blackPlayerName, pgn, result, null);
        GameEntity game = gameMapper.createGameDtoToGame(gameDto);

        assertEquals(game.getWhitePlayerName(), gameDto.whitePlayerName(),
                "White player name should match");
        assertEquals(game.getBlackPlayerName(), gameDto.blackPlayerName(),
                "Black player name should match");
        assertEquals(game.getResult(), gameDto.result(),
                "Result should match");
        assertEquals(game.getPgn(), gameDto.pgn(),
                "Pgn should match");
    }

    @Test
    @DisplayName("Correctly map UpdateGameDto to GameEntity")
    void shouldMapUpdateGameDtoToGameEntity() {
        UpdateGameDto gameDto = new UpdateGameDto(gameId, whitePlayerName, blackPlayerName, pgn, result, null);
        GameEntity game = gameMapper.updateGameDtoToGame(gameDto, GameEntity.builder().uuid(gameId).build());

        assertEquals(game.getUuid(), gameDto.uuid(),
                "Uuid should match");
        assertEquals(game.getWhitePlayerName(), gameDto.whitePlayerName(),
                "White player name should match");
        assertEquals(game.getBlackPlayerName(), gameDto.blackPlayerName(),
                "Black player name should match");
        assertEquals(game.getResult(), gameDto.result(),
                "Result should match");
        assertEquals(game.getPgn(), gameDto.pgn(),
                "Pgn should match");
    }

    @Test
    @DisplayName("Correctly map list of GameEntity to GameDto")
    void shouldMapListGameEntityToGameDto() {
        List<GameEntity> games = List.of(game, game);
        List<GameDto> gameDtos = gameMapper.listOfGamesToGameDto(games);

        assertEquals(2, gameDtos.size(),
                "List size size should match");
        assertEquals(game.getUuid(), gameDtos.getFirst().uuid(),
                "Uuid should match");
        assertEquals(game.getWhitePlayerName(), gameDtos.getFirst().whitePlayerName(),
                "White player name should match");
        assertEquals(game.getBlackPlayerName(), gameDtos.getFirst().blackPlayerName(),
                "Black player name should match");
        assertEquals(game.getResult(), gameDtos.getFirst().result(),
                "Result should match");
        assertEquals(game.getPgn(), gameDtos.getFirst().pgn(),
                "Pgn should match");
    }

    @Test
    @DisplayName("Correctly map Page GameEntity to Page GameDto")
    void shouldMapPageGameEntityToGameDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<GameEntity> allGames = List.of(game);
        Page<GameEntity> games = new PageImpl<>(allGames, pageable, allGames.size());
        Page<GameDto> gamesDto = gameMapper.pageGameEntityToPageGameDto(games);

        assertAll("Page mapper assertions",
                () -> assertEquals(1, gamesDto.getTotalElements(),
                        "Total elements should match"),
                () -> assertEquals(game.getUuid(), gamesDto.getContent().getFirst().uuid(),
                        "Uuid should match"),
                () -> assertEquals(game.getWhitePlayerName(), gamesDto.getContent().getFirst().whitePlayerName(),
                        "White player name should match"),
                () -> assertEquals(game.getBlackPlayerName(), gamesDto.getContent().getFirst().blackPlayerName(),
                        "Black player name should match"),
                () -> assertEquals(game.getResult(), gamesDto.getContent().getFirst().result(),
                        "Result should match"),
                () -> assertEquals(game.getPgn(), games.getContent().getFirst().getPgn(),
                        "Png should match"),
                () -> assertNull(game.getTournament(), "Tournament should be null"));
    }
}
