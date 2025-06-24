package org.csc.chessclub.service.tournament;

import org.csc.chessclub.model.TournamentEntity;

public interface TournamentService {
    TournamentEntity create(TournamentEntity tournament);
    TournamentEntity update(TournamentEntity tournament);
}
