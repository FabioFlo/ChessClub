package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.model.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertAll("Map tournament to dto assertions",
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
