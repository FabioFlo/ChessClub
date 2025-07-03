package org.csc.chessclub.service.tournament;

import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TournamentService {
    TournamentDto create(CreateTournamentDto tournament);

    TournamentDto update(UpdateTournamentDto tournamentDto);

    TournamentEntity getById(UUID uuid);

    Page<TournamentEntity> getAll(Pageable pageable);

    Page<TournamentEntity> getAllAvailable(Pageable pageable);

    void delete(UUID uuid);
}
