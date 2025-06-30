package org.csc.chessclub.service.tournament;

import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.TournamentServiceException;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceExceptionTests {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private EventRepository eventRepository;
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
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .event(EventEntity.builder().uuid(uuid).build())
                .build();

    }

    @Test
    @DisplayName("Create Tournament - Throw if end date is before start date")
    void testCreateTournament_whenEndDateIsBeforeStartDate_throwCustomTournamentException() {
        LocalDate invalidStartDate = LocalDate.parse("2019-01-01");
        tournament.setStartDate(invalidStartDate);

        TournamentServiceException exception = assertThrows(TournamentServiceException.class,
                () -> tournamentService.create(tournament));

        assertEquals("Start date must be before end date", exception.getMessage());
    }

    @Test
    @DisplayName("Update Tournament - Throw if end date is before start date")
    void testUpdateTournament_whenEndDateIsBeforeStartDate_throwCustomTournamentException() {
        when(tournamentRepository.existsById(tournament.getUuid())).thenReturn(true);
        LocalDate invalidStartDate = LocalDate.parse("2019-01-01");
        tournament.setStartDate(invalidStartDate);

        TournamentServiceException exception = assertThrows(TournamentServiceException.class,
                () -> tournamentService.update(tournament));

        assertEquals("Start date must be before end date", exception.getMessage());
    }

    @Test
    @DisplayName("Create tournament - throw if event id does not exist")
    void testCreateTournament_whenEventUuidPassedNotExists_thenThrowCustomNotFoundException() {
        tournament.setEvent(EventEntity.builder().uuid(UUID.randomUUID()).build());
        when(eventRepository.existsById(tournament.getEvent().getUuid())).thenReturn(false);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> tournamentService.create(tournament));

        assertEquals(NotFoundMessage.EVENT_WITH_UUID.format(
                tournament.getEvent().getUuid()), exception.getMessage());
    }

    @Test
    @DisplayName("Update tournament - throw if event id does not exist")
    void testUpdateTournament_whenEventUuidPassedNotExists_thenThrowCustomNotFoundException() {
        when(tournamentRepository.existsById(tournament.getUuid())).thenReturn(true);
        tournament.setEvent(EventEntity.builder().uuid(UUID.randomUUID()).build());
        when(eventRepository.existsById(tournament.getEvent().getUuid())).thenReturn(false);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> tournamentService.update(tournament));

        assertEquals(NotFoundMessage.EVENT_WITH_UUID.format(
                tournament.getEvent().getUuid()), exception.getMessage());
    }
}
