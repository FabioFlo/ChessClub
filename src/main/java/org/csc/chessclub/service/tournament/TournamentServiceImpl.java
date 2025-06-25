package org.csc.chessclub.service.tournament;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.TournamentServiceException;
import org.csc.chessclub.model.TournamentEntity;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final String dateExceptionMessage = "Start date must be before end date";

    @Override
    public TournamentEntity create(TournamentEntity tournament) {
        tournament.setAvailable(true);
        if (startDateNotBeforeEndDate(tournament.getStartDate(), tournament.getEndDate())) {
            throw new TournamentServiceException(dateExceptionMessage);
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public TournamentEntity update(TournamentEntity tournament) {
        if (!tournamentRepository.existsById(tournament.getUuid())) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournament.getUuid()));
        }
        if (startDateNotBeforeEndDate(tournament.getStartDate(), tournament.getEndDate())) {
            throw new TournamentServiceException(dateExceptionMessage);
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

    @Override
    public Page<TournamentEntity> getAll(Pageable pageable) {
        return tournamentRepository.findAll(pageable);
    }

    @Override
    public Page<TournamentEntity> getAllAvailable(Pageable pageable) {
        return tournamentRepository.getDistinctByAvailableIsTrue(pageable);
    }

    @Override
    public void delete(UUID uuid) {
        Optional<TournamentEntity> tournamentEntity = tournamentRepository.findById(uuid);
        if (tournamentEntity.isEmpty()) {
            throw new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(uuid));
        }
        tournamentEntity.get().setAvailable(false);
        tournamentRepository.save(tournamentEntity.get());
    }

    private boolean startDateNotBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        return !startDate.isBefore(endDate);
    }
}
