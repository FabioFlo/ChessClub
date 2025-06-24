package org.csc.chessclub.service.tournament;

import org.csc.chessclub.model.TournamentEntity;

import java.util.UUID;

public interface TournamentService {
    TournamentEntity create(TournamentEntity tournament);
    TournamentEntity update(TournamentEntity tournament);

    TournamentEntity getById(UUID uuid);
}
