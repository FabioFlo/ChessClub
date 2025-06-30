package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentMapperTests {
    private final TournamentMapper tournamentMapper = TournamentMapper.INSTANCE;

    private TournamentEntity tournament;
    private static final String TITLE = "Tournament";
    private static final String DESCRIPTION = "Description";
    private static final LocalDate START_DATE = LocalDate.parse("2019-01-01");
    private static final LocalDate END_DATE = LocalDate.parse("2018-01-03");
    private static final UUID EVENT_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();

        tournament = TournamentEntity.builder()
                .uuid(uuid)
                .title(TITLE)
                .description(DESCRIPTION)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .build();

    }

    @Test
    @DisplayName("Map TournamentEntity to TournamentDto")
    void shouldMapTournamentEntityToTournamentDto() {
        TournamentDto tournamentDto = tournamentMapper.tournamentToTournamentDto(tournament);

        assertAll("Map tournament to dto assertions",
                () -> assertEquals(tournament.getUuid(), tournamentDto.uuid(),
                        "UUID should be equal"),
                () -> assertEquals(tournament.getTitle(), tournamentDto.title(),
                        "Title should be equal"),
                () -> assertEquals(tournament.getStartDate(), tournamentDto.startDate(),
                        "Start date should be equal"),
                () -> assertEquals(tournament.getEndDate(), tournamentDto.endDate(),
                        "End game should be equal"),
                () -> assertEquals(tournament.getDescription(), tournamentDto.description(),
                        "Description should be equal"));

    }

    @Test
    @DisplayName("Map CreateTournamentDto to TournamentEntity")
    void shouldMapCreateTournamentDtoToTournamentEntity() {
        CreateTournamentDto tournamentDto = new CreateTournamentDto(TITLE, START_DATE, END_DATE, DESCRIPTION, EVENT_ID);
        TournamentEntity tournamentEntity = tournamentMapper.createTournamentDtoToTournamentEntity(tournamentDto);

        assertAll("Map create tournament dto to entity assertions",
                () -> assertEquals(tournamentEntity.getTitle(), tournamentDto.title(),
                        "Title should be equal"),
                () -> assertEquals(tournamentEntity.getStartDate(), tournamentDto.startDate(),
                        "Start date should be equal"),
                () -> assertEquals(tournamentEntity.getEndDate(), tournamentDto.endDate(),
                        "End game should be equal"),
                () -> assertEquals(tournamentEntity.getDescription(), tournamentDto.description(),
                        "Description should be equal"),
                () -> assertEquals(tournamentEntity.getEvent().getUuid(), tournamentDto.eventId(),
                        "Event Uuid should be equal"));
    }

    @Test
    @DisplayName("Map page of TournamentEntity to page of TournamentDto")
    void shouldMapPageOfTournamentEntityToPageOfTournamentDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<TournamentEntity> listTournaments = List.of(tournament);
        Page<TournamentEntity> pageOfTournament = new PageImpl<>(listTournaments, pageable, listTournaments.size());

        Page<TournamentDto> result = tournamentMapper.pageTournamentEntityToPageTournamentDto(pageOfTournament);

        assertAll("Page entity to page dto assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(), "Should be one tournament"),
                () -> assertEquals(0, result.getNumber(), "Page number should be equal to zero"),
                () -> assertEquals(tournament.getUuid(), result.getContent().getFirst().uuid(),
                        "Tournament UUID should be equal"));
    }

    @Test
    @DisplayName("Map UpdateTournamentDto to TournamentEntity")
    void shouldMapUpdateTournamentDtoToTournamentEntity() {
        UpdateTournamentDto tournamentDto = new UpdateTournamentDto(UUID.randomUUID(), TITLE, START_DATE, END_DATE, DESCRIPTION, EVENT_ID);
        TournamentEntity tournamentEntity = tournamentMapper.updateTournamentToTournament(tournamentDto);
        assertAll("Map update tournament to entity assertions",
                () -> assertEquals(tournamentEntity.getUuid(), tournamentDto.uuid(),
                        "UUID should be equal"),
                () -> assertEquals(tournamentEntity.getTitle(), tournamentDto.title(),
                        "Title should be equal"),
                () -> assertEquals(tournamentEntity.getStartDate(), tournamentDto.startDate(),
                        "Start date should be equal"),
                () -> assertEquals(tournamentEntity.getEndDate(), tournamentDto.endDate(),
                        "End game should be equal"),
                () -> assertEquals(tournamentEntity.getDescription(), tournamentDto.description(),
                        "Description should be equal"),
                () -> assertEquals(tournamentEntity.getEvent().getUuid(), tournamentDto.eventId(),
                        "Event Uuid should be equal"));
    }
}
