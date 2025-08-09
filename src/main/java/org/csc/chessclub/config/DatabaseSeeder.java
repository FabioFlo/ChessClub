package org.csc.chessclub.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.csc.chessclub.config.data.EventMockData;
import org.csc.chessclub.config.data.GameMockData;
import org.csc.chessclub.config.data.TournamentMockData;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.repository.GameRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "!test"})
@Order(2)
@Log
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    @Value("${init.data}")
    private Boolean initData;

    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;
    private final GameRepository gameRepository;

    private final EventMockData eventMockData;
    private final TournamentMockData tournamentMockData;
    private final GameMockData gameMockData;

    @Override
    public void run(String... args) {
        log.info("Starting Database Seeder: " + initData);
        if (initData && eventRepository.count() == 0 && tournamentRepository.count() == 0 && gameRepository.count() == 0) {
            log.info("Initializing database...");
            eventMockData.listOfEvents();
            tournamentMockData.listOfTournaments();
            gameMockData.listOfGames();
        } else {
            log.info("Nothing to initialize.");
        }
    }
}