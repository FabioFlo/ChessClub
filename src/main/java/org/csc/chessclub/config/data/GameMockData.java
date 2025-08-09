package org.csc.chessclub.config.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.game.GameEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.GameRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.csc.chessclub.config.data.TournamentMockData.*;

@Component
@Profile("local")
@RequiredArgsConstructor
@Log
public final class GameMockData {
    private final GameRepository gameRepository;
    private final TournamentRepository tournamentRepository;

    public void listOfGames() {
        List<TournamentEntity> tournaments = tournamentRepository.findAll();
        TournamentEntity amazeTournamentFirst = findTournamentByTitle(AMAZING_TOURNAMENT_TITLE_FIRST, tournaments);
        TournamentEntity amazeTournamentSecond = findTournamentByTitle(AMAZING_TOURNAMENT_TITLE_SECOND, tournaments);
        TournamentEntity worldCup2023 = findTournamentByTitle(WORLD_CUP_2023, tournaments);

        List<GameEntity> gameEntities = List.of(
                GameEntity.builder()
                        .pgn("""
                                [Event "Casual game"]
                                [Site "New Orleans, LA USA"]
                                [Date "1848.??.??"]
                                [EventDate "1848.??.??"]
                                [Round "?"]
                                [Result "1-0"]
                                [White "Paul Morphy"]
                                [Black "Alonzo Morphy"]
                                [ECO "C33"]
                                [WhiteElo "?"]
                                [BlackElo "?"]
                                [PlyCount "35"]
                                
                                1. e4 {Some sources indicate 1847.} e5 2. f4 exf4 3. Bc4 Qh4+
                                4. Kf1 Bc5 5. d4 Bb6 6. Nf3 Qe7 7. Nc3 Nf6 8. Qd3 c6 9. Bxf4
                                d5 10. exd5 O-O 11. d6 Qd8 12. Re1 Re8 13. Ng5 Rxe1+ 14. Kxe1
                                Qe8+ 15. Kd2 Be6 16. Re1 Nbd7 17. Nxe6 fxe6 18. Rxe6 1-0""")
                        .whitePlayerName("Paul Morphy")
                        .blackPlayerName("Alonzo Morphy")
                        .result(Result.WhiteWon)
                        .available(true)
                        .tournament(amazeTournamentFirst)
                        .build(),
                GameEntity.builder()
                        .pgn("""
                                [Event "Rook Odds game"]
                                [Site "New Orleans, LA USA"]
                                [Date "1849.??.??"]
                                [EventDate "?"]
                                [Round "?"]
                                [Result "1-0"]
                                [White "Paul Morphy"]
                                [Black "Charles Le Carpentier"]
                                [ECO "000"]
                                [WhiteElo "?"]
                                [BlackElo "?"]
                                [PlyCount "25"]
                                [SetUp "1"]
                                [FEN "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR w Kkq - 0 1"]
                                
                                1.e4 e5 2.Nf3 Nc6 3.d4 exd4 4.Bc4 Bb4+ 5.c3 dxc3 6.O-O cxb2
                                7.Bxb2 Bf8 8.e5 d6 9.Re1 dxe5 10.Nxe5 Qxd1 11.Bxf7+ Ke7
                                12.Ng6+ Kxf7 13.Nxh8# 1-0""")
                        .whitePlayerName("Paul Morphy")
                        .blackPlayerName("Charles Le Carpentier")
                        .result(Result.WhiteWon)
                        .available(true)
                        .tournament(amazeTournamentSecond)
                        .build(),
                GameEntity.builder()
                        .pgn("""
                                [Event "FIDE World Cup 2023"]
                                [Site "Chess.com"]
                                [Date "2023.08.24"]
                                [Round "08-03"]
                                [White "Praggnanandhaa R"]
                                [Black "Carlsen, Magnus"]
                                [Result "0-1"]
                                [WhiteElo "2707"]
                                [BlackElo "2835"]
                                [TimeControl "40/5400+30:1800+30"]
                                [Link "https://www.chess.com/events/2023-fide-chess-world-cup/08-03/Praggnanandhaa_R-Carlsen_Magnus"]
                                
                                1. e4 {[%clk 0:25:11]} 1... e5 {[%clk 0:25:07]} 2. Nf3 {[%clk 0:25:17]} 2... Nc6
                                {[%clk 0:25:11]} 3. Bc4 {[%clk 0:25:24]} 3... Nf6 {[%clk 0:25:16]} 4. d3 {[%clk
                                0:25:32]} 4... Bc5 {[%clk 0:25:23]} 5. a4 {[%clk 0:25:36]} 5... d6 {[%clk
                                0:24:42]} 6. O-O {[%clk 0:25:33]} 6... a5 {[%clk 0:23:14]} 7. Be3 {[%clk
                                0:25:41]} 7... Bxe3 {[%clk 0:22:23]} 8. fxe3 {[%clk 0:25:49]} 8... O-O {[%clk
                                0:22:30]} 9. Nbd2 {[%clk 0:25:57]} 9... Ne7 {[%clk 0:22:28]} 10. Nh4 {[%clk
                                0:24:03]} 10... c6 {[%clk 0:22:35]} 11. Qe1 {[%clk 0:22:58]} 11... d5 {[%clk
                                0:18:53]} 12. Bb3 {[%clk 0:22:57]} 12... Qd6 {[%clk 0:18:58]} 13. Qg3 {[%clk
                                0:19:54]} 13... Nh5 {[%clk 0:14:32]} 14. Qg5 {[%clk 0:18:38]} 14... g6 {[%clk
                                0:14:19]} 15. Nf5 {[%clk 0:18:05]} 15... Bxf5 {[%clk 0:13:06]} 16. exf5 {[%clk
                                0:18:12]} 16... Kg7 {[%clk 0:12:33]} 17. Kh1 {[%clk 0:11:48]} 17... Qf6 {[%clk
                                0:10:05]} 18. Qxf6+ {[%clk 0:10:23]} 18... Nxf6 {[%clk 0:10:13]} 19. fxg6 {[%clk
                                0:09:41]} 19... hxg6 {[%clk 0:10:20]} 20. e4 {[%clk 0:09:15]} 20... dxe4 {[%clk
                                0:09:46]} 21. dxe4 {[%clk 0:09:00]} 21... Rad8 {[%clk 0:06:52]} 22. Rf2 {[%clk
                                0:07:44]} 22... Rd4 {[%clk 0:05:51]} 23. Raf1 {[%clk 0:07:27]} 23... Neg8 {[%clk
                                0:05:59]} 24. c3 {[%clk 0:07:04]} 24... Rd7 {[%clk 0:06:03]} 25. Re2 {[%clk
                                0:07:07]} 25... Re7 {[%clk 0:04:48]} 26. Bc2 {[%clk 0:05:34]} 26... Nd7 {[%clk
                                0:04:41]} 27. Nc4 {[%clk 0:05:12]} 27... Ra8 {[%clk 0:04:49]} 28. g4 {[%clk
                                0:03:32]} 28... f6 {[%clk 0:04:19]} 29. Rg2 {[%clk 0:03:23]} 29... Nh6 {[%clk
                                0:02:42]} 30. g5 {[%clk 0:02:53]} 30... fxg5 {[%clk 0:02:50]} 31. Rxg5 {[%clk
                                0:03:00]} 31... Nf7 {[%clk 0:02:58]} 32. Rg2 {[%clk 0:03:00]} 32... Re6 {[%clk
                                0:02:51]} 33. Rd2 {[%clk 0:01:33]} 33... Rf6 {[%clk 0:02:52]} 34. Rxf6 {[%clk
                                0:00:52]} 34... Nxf6 {[%clk 0:03:00]} 35. b4 {[%clk 0:00:46]} 35... axb4 {[%clk
                                0:03:08]} 36. cxb4 {[%clk 0:00:56]} 36... Kf8 {[%clk 0:02:27]} 37. Kg2 {[%clk
                                0:00:34]} 37... Ke7 {[%clk 0:02:32]} 38. a5 {[%clk 0:00:40]} 38... Rh8 {[%clk
                                0:02:35]} 39. Re2 {[%clk 0:00:22]} 39... Nh5 {[%clk 0:02:36]} 40. Kg1 {[%clk
                                0:00:22]} 40... Nf4 {[%clk 0:02:41]} 41. Rd2 {[%clk 0:00:18]} 41... Rh3 {[%clk
                                0:02:49]} 42. a6 {[%clk 0:00:16]} 42... bxa6 {[%clk 0:02:55]} 43. Ba4 {[%clk
                                0:00:19]} 43... Rc3 {[%clk 0:02:43]} 44. Na5 {[%clk 0:00:28]} 44... Ng5 {[%clk
                                0:02:48]} 45. Rc2 {[%clk 0:00:25]} 45... Ngh3+ {[%clk 0:02:23]} 46. Kf1 {[%clk
                                0:00:19]} 46... Ra3 {[%clk 0:02:32]} 47. Nxc6+ {[%clk 0:00:15]} 47... Kf6 {[%clk
                                0:02:39]} 0-1""")
                        .whitePlayerName("Praggnanandhaa R")
                        .blackPlayerName("Carlsen, Magnus")
                        .result(Result.BlackWon)
                        .available(true)
                        .tournament(worldCup2023)
                        .build()
        );
        log.info("Saving games.");
        gameRepository.saveAll(gameEntities);
    }

    private TournamentEntity findTournamentByTitle(String title, List<TournamentEntity> tournaments) {
        return tournaments.stream()
                .filter(e -> e.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}
