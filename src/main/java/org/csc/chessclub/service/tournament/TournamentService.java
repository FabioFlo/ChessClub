package org.csc.chessclub.service.tournament;

import org.csc.chessclub.model.TournamentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TournamentService {
    TournamentEntity create(TournamentEntity tournament);

    TournamentEntity update(TournamentEntity tournament);

    TournamentEntity getById(UUID uuid);

    Page<TournamentEntity> getAll(Pageable pageable);

    Page<TournamentEntity> getAllAvailable(Pageable pageable);

    void delete(UUID uuid);
}
