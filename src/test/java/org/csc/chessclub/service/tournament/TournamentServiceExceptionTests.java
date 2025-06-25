package org.csc.chessclub.service.tournament;

import org.csc.chessclub.exception.TournamentServiceException;
import org.csc.chessclub.model.TournamentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceExceptionTests {

    @InjectMocks
    private TournamentServiceImpl tournamentService;

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
    @DisplayName("Create Tournament - Throw if end date is before start date")
    void testCreateTournament_whenEndDateIsBeforeStartDate_throwCustomTournamentException() {

       TournamentServiceException exception = assertThrows(TournamentServiceException.class,
               () -> tournamentService.create(tournament));

       assertEquals("Start date must be before end date", exception.getMessage());
    }
}
