package org.csc.chessclub.service.tournament;

import org.csc.chessclub.model.TournamentEntity;
import org.csc.chessclub.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceUnitTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private TournamentEntity tournament;
    private TournamentEntity tournament1;
    private TournamentEntity tournament2;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        UUID uuid = UUID.randomUUID();
        tournament = TournamentEntity.builder()
                .uuid(uuid)
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .build();

        tournament1 = TournamentEntity.builder()
                .uuid(UUID.randomUUID())
                .title("Tournament1")
                .description("Description1")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(true)
                .build();

        tournament2 = TournamentEntity.builder()
                .uuid(UUID.randomUUID())
                .title("Tournament2")
                .description("Description2")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .available(false)
                .build();
    }

    @Test
    @DisplayName("Create tournament")
    void testCreateTournament_whenTournamentDetailsProvided_return() {
        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(tournament);

        TournamentEntity newTournament = tournamentService.create(tournament);

        assertAll("Create tournament assertions",
                () -> assertNotNull(newTournament, "Tournament should not be null"),
                () -> assertNotNull(newTournament.getUuid(), "New tournament should not be null"),
                () -> assertEquals(tournament.getTitle(), newTournament.getTitle(), "Title should be equal"),
                () -> assertTrue(tournament.isAvailable(), "Tournament should be available"),
                () -> verify(tournamentRepository, times(1)).save(tournament));
    }

    @Test
    @DisplayName("Update tournament")
    void testUpdateTournament_whenTournamentDetailsProvided_returnTournament() {
        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(tournament);
        when(tournamentRepository.existsById(tournament.getUuid())).thenReturn(true);

        tournament.setTitle("Updated Tournament");

        TournamentEntity updatedTournament = tournamentService.update(tournament);

        assertAll("Update tournament assertions",
                () -> assertNotNull(updatedTournament, "Tournament should be returned"),
                () -> assertEquals(tournament.getTitle(), updatedTournament.getTitle(), "Title should be equal"),
                () -> verify(tournamentRepository, times(1)).save(updatedTournament));
    }

    @Test
    @DisplayName("Get tournament by id")
    void testGetTournamentById_whenTournamentIdProvided_returnTournament() {
        when(tournamentRepository.findById(tournament.getUuid())).thenReturn(Optional.of(tournament));

        TournamentEntity retrievedTournament = tournamentService.getById(tournament.getUuid());

        assertAll("Get tournament by id assertions",
                () -> assertNotNull(retrievedTournament, "Tournament should not be null"),
                () -> assertEquals(tournament, retrievedTournament, "Tournament should be equal"),
                () -> verify(tournamentRepository, times(1)).findById(tournament.getUuid()));
    }

    @Test
    @DisplayName("Get all tournaments")
    void testGetAll_whenPageableProvided_thenReturnPaginatedTournaments() {
        List<TournamentEntity> tournaments = List.of(tournament, tournament1, tournament2);
        Page<TournamentEntity> pagedTournaments = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.findAll(pageable)).thenReturn(pagedTournaments);

        Page<TournamentEntity> result = tournamentService.getAll(pageable);

        assertAll("Get all assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(3, result.getTotalElements(), "Result should contain two tournaments"));
    }

    @Test
    @DisplayName("Get all available tournaments")
    void testGetAllAvailable_whenPageableProvided_thenReturnPaginatedTournamentsWithAvailableTrue() {
        List<TournamentEntity> tournaments = List.of(tournament1);
        Page<TournamentEntity> pagedTournaments = new PageImpl<>(tournaments, pageable, tournaments.size());

        when(tournamentRepository.getDistinctByAvailableIsTrue(pageable)).thenReturn(pagedTournaments);

        Page<TournamentEntity> result = tournamentService.getAllAvailable(pageable);

        assertAll("Get all assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(), "Result should contain two tournaments"),
                () -> assertTrue(result.getContent().getFirst().isAvailable(), "Available should be true"));
    }

    @Test
    @DisplayName("Delete tournament")
    void testDeleteTournament_whenTournamentUuidProvided_setAvailableToFalse() {
        when(tournamentRepository.findById(tournament.getUuid())).thenReturn(Optional.ofNullable(tournament));
        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(tournament);

        tournament.setAvailable(false);
        assertAll("Delete tournament assertions",
                () -> assertFalse(tournament.isAvailable(),
                        "Available should be false"),
                () -> assertDoesNotThrow(() -> tournamentService.delete(tournament.getUuid()),
                        "Should not throw an exception"),
                () -> verify(tournamentRepository, times(1)).save(tournament));


    }
}
