package org.csc.chessclub.service.tournament;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.model.TournamentEntity;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;

    @Override
    public TournamentEntity create(TournamentEntity tournament) {
        tournament.setAvailable(true);
        return tournamentRepository.save(tournament);
    }

    @Override
    public TournamentEntity update(TournamentEntity tournament) {
        if (!tournamentRepository.existsById(tournament.getUuid())) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournament.getUuid()));
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public TournamentEntity getById(UUID uuid) {
        Optional<TournamentEntity> tournamentEntity = tournamentRepository.findById(uuid);
        if (tournamentEntity.isEmpty()) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(uuid));
        }
        return tournamentEntity.get();
    }
}
