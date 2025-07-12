package org.csc.chessclub.repository;

import jakarta.transaction.Transactional;
import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentRepositoryTests extends TestContainerConfig {

    @Autowired
    private TournamentRepository tournamentRepository;
    private TournamentEntity tournament;

    @BeforeEach
    void setUp() {
        //TODO: think about a custom repo to get only specific info
        //TODO: Possible views table to summarize the important data from event and tournament
        tournament = TournamentEntity.builder()
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(true)
                .build();

        TournamentEntity tournament1 = TournamentEntity.builder()
                .title("Tournament1")
                .description("Description1")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(false)
                .build();

        tournamentRepository.save(tournament);
        tournamentRepository.save(tournament1);

    }

    @Test
    @DisplayName("Find tournament by title")
    void testGetTournamentByTitle_whenTitleProvided_returnTournamentIfExist() {
        String title = "Tournament";

        List<TournamentEntity> result = tournamentRepository.getTournamentEntitiesByTitleAndAvailableTrue(title);

        assertAll("Get by title assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.size(), "Should contain one tournament"),
                () -> assertTrue(result.stream().allMatch(TournamentEntity::isAvailable), "Result should be available"),
                () -> assertEquals(title, result.getFirst().getTitle(), "Title should be equal"));
    }

    @Test
    @DisplayName("Find all available tournaments")
    void testGetAllAvailableTournaments_returnTournamentsWithAvailableTrue() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TournamentEntity> result = tournamentRepository.findByAvailableIsTrue(pageable);

        boolean allAvailable = result.getContent().stream().allMatch(TournamentEntity::isAvailable);

        assertAll("Get all available tournaments",
                () -> assertFalse(result.isEmpty(), "Result should not be null"),
                () -> assertTrue(allAvailable, "All tournaments should be available"));
    }

    @Test
    @DisplayName("Set available to false")
    @Transactional
    void testSetAvailableFalse_whenGivenUuid_resultShouldBeOne() {
        int result = tournamentRepository.setAvailableFalse(tournament.getUuid());

        assertEquals(1, result, "Should return 1");
    }

    //TODO: set all tournament available false where event uuid = eventUuid
}
