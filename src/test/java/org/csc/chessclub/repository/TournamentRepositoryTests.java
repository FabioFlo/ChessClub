package org.csc.chessclub.repository;

import jakarta.transaction.Transactional;
import org.csc.chessclub.controller.BaseTestConfiguration;
import org.csc.chessclub.model.event.EventEntity;
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

public class TournamentRepositoryTests extends BaseTestConfiguration {

    @Autowired
    private TournamentRepository tournamentRepository;
    private TournamentEntity tournament;
    @Autowired
    private EventRepository eventRepository;
    private EventEntity event;

    @BeforeEach
    void setUp() {
        createBaseTestData();
    }

    @Override
    public void createBaseTestData() {
        event = eventRepository.save(EventEntity
                .builder()
                .description("Event description")
                .announcementPDF("")
                .author("Billy")
                .createdAt(LocalDate.parse("2022-02-01"))
                .title("Titolo evento")
                .available(true)
                .build());

        tournament = TournamentEntity.builder()
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(true)
                .event(event)
                .build();

        TournamentEntity tournament1 = TournamentEntity.builder()
                .title("Tournament_1")
                .description("Description1")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(false)
                .event(event)
                .build();

        tournamentRepository.save(tournament);
        tournamentRepository.save(tournament1);
    }


    @Test
    @DisplayName("Find tournament by title")
    void testGetTournamentByTitle_whenTitleProvided_returnTournamentIfExist() {
        String title = "Tournament";

        List<TournamentEntity> result = tournamentRepository.getTournamentEntitiesByTitleAndAvailableTrue(
                title);

        assertAll("Get by title assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(2, result.size(), "Should contain one tournament"),
                () -> assertTrue(result.stream().allMatch(TournamentEntity::isAvailable),
                        "Result should be available"),
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

    @Test
    @DisplayName("Find all available tournaments by Event uuid")
    void testGetAllTournaments_WhenEventUuidProvided_returnTournamentsWithEqualEventUuidAndAvailableTrue() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TournamentEntity> result = tournamentRepository.findTournamentEntitiesByEvent_UuidAndAvailableTrue(event.getUuid(), pageable);

        boolean allAvailable = result.getContent().stream().allMatch(TournamentEntity::isAvailable);

        assertAll("Get all available tournaments",
                () -> assertFalse(result.isEmpty(), "Result should not be null"),
                () -> assertTrue(allAvailable, "All tournaments should be available"),
                () -> assertEquals(event.getUuid(), result.getContent().getFirst().getEvent().getUuid(), "Event should be equal"));
    }
}
