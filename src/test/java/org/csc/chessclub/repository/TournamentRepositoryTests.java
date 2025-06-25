package org.csc.chessclub.repository;

import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.model.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentRepositoryTests extends TestContainerConfig {

    @Autowired
    private TournamentRepository tournamentRepository;

    @BeforeEach
    void setUp() {
        //TODO: think about a custom repo to get only specific info
        //TODO: Possible views table to summarize the important data from event and tournament
        TournamentEntity tournament = TournamentEntity.builder()
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .build();

        TournamentEntity tournament1 = TournamentEntity.builder()
                .title("Tournament1")
                .description("Description1")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .build();

        tournamentRepository.save(tournament);
        tournamentRepository.save(tournament1);

    }

    @Test
    @DisplayName("Find tournament by title")
    void testGetTournamentByTitle_whenTitleProvided_returnTournamentIfExist() {
        String title = "Tournament";

        List<TournamentEntity> result = tournamentRepository.getTournamentEntitiesByTitle(title);

        assertAll("Get by title assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.size(), "Should contain one tournament"),
                () -> assertEquals(title, result.getFirst().getTitle(), "Title should be equal"));
    }

    //TODO: return only available entities (this should be default for user)
}
