package org.csc.chessclub.service.tournament;

import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.TournamentServiceException;
import org.csc.chessclub.mapper.TournamentMapper;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceExceptionTests {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private TournamentServiceImpl tournamentService;
    @Spy
    private final TournamentMapper tournamentMapper = Mappers.getMapper(TournamentMapper.class);

    private TournamentEntity tournament;
    private UpdateTournamentDto tournamentDto;

    private UUID uuid = UUID.randomUUID();
    private String title = "Tournament";
    private String description = "description";
    private LocalDate endDate = LocalDate.parse("2019-01-01");

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        title = "Tournament";
        description = "description";
        LocalDate startDate = LocalDate.parse("2018-01-01");
        endDate = LocalDate.parse("2019-01-01");

        tournament = TournamentEntity.builder()
                .uuid(uuid)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .event(EventEntity.builder().uuid(uuid).build())
                .build();

        tournamentDto = new UpdateTournamentDto(uuid, title, startDate, endDate, description, UUID.randomUUID());
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
        LocalDate invalidStartDate = LocalDate.parse("2019-01-01");
        tournamentDto = new UpdateTournamentDto(uuid, title, invalidStartDate, endDate, description, UUID.randomUUID());

        when(tournamentRepository.findById(tournamentDto.uuid()))
                .thenReturn(Optional.ofNullable(tournament));

        TournamentServiceException exception = assertThrows(TournamentServiceException.class,
                () -> tournamentService.update(tournamentDto));

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
        when(tournamentRepository.findById(tournamentDto.uuid()))
                .thenReturn(Optional.ofNullable(tournament));
        when(eventRepository.existsById(tournamentDto.eventId())).thenReturn(false);

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> tournamentService.update(tournamentDto));

        assertEquals(NotFoundMessage.EVENT_WITH_UUID.format(
                tournament.getEvent().getUuid()), exception.getMessage());
    }

    @Test
    @DisplayName("Get by id - theow when Tournament not found")
    void testGetById_whenTournamentNotFound_thenThrowCustomNotFoundException() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> tournamentService.getById(tournament.getUuid()));
        assertEquals(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournament.getUuid()), exception.getMessage());

        verify(tournamentRepository, times(1)).findById(tournament.getUuid());
    }

}
