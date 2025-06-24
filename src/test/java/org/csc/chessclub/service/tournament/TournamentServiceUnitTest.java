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


import java.time.LocalDate;
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

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        tournament = TournamentEntity.builder()
                .uuid(uuid)
                .title("Tournament")
                .description("Description")
                .startDate(LocalDate.parse("2018-01-01"))
                .endDate(LocalDate.parse("2018-01-03"))
                .build();
    }

    //TODO: check if startDate is before endDate in create and update (throw if is not)

    @Test
    @DisplayName("Create tournament")
    void testCreateTournament_whenTournamentDetailsProvided_return() {
        when(tournamentRepository.save(any(TournamentEntity.class))).thenReturn(tournament);

        TournamentEntity newTournament = tournamentService.create(tournament);

        assertAll("Create tournament assertions",
                () -> assertNotNull(newTournament, "Tournament should not be null"),
                () -> assertNotNull(newTournament.getUuid(), "New tournament should not be null"),
                () -> assertEquals(tournament.getTitle(), newTournament.getTitle(), "Title should be equal"),
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
}
