package org.csc.chessclub.service.tournament;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.model.TournamentEntity;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;

    @Override
    public TournamentEntity create(TournamentEntity tournament) {
        return tournamentRepository.save(tournament);
    }
}
