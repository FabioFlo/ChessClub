package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.model.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentMapperTests {
    private final TournamentMapper tournamentMapper = TournamentMapper.INSTANCE;

    private TournamentEntity tournament;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        tournament = TournamentEntity.builder()
                .uuid(uuid)
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2019-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
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
}
