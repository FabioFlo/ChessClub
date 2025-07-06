package org.csc.chessclub.service.tournament;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TournamentService {
    TournamentDto create(CreateTournamentDto tournament);

    TournamentDto update(UpdateTournamentDto tournamentDto);

    TournamentDto getById(UUID uuid);

    Page<TournamentDto> getAll(Pageable pageable);

    Page<TournamentDto> getAllAvailable(Pageable pageable);

    void delete(UUID uuid);
}
