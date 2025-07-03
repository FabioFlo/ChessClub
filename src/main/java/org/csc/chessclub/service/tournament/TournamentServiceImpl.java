package org.csc.chessclub.service.tournament;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.TournamentServiceException;
import org.csc.chessclub.mapper.TournamentMapper;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.EventRepository;
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
    private final EventRepository eventRepository;
    private final TournamentMapper tournamentMapper;

    @Override
    public TournamentEntity create(TournamentEntity tournament) {
        tournament.setAvailable(true);
        return validTournamentDetails(tournament);
    }

    @Override
    public TournamentDto update(UpdateTournamentDto tournamentDto) {
        TournamentEntity tournament = tournamentRepository.findById(tournamentDto.uuid())
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.TOURNAMENT_WITH_UUID.format(tournamentDto.uuid())));

        tournamentMapper.updateTournamentToTournament(tournamentDto, tournament);

        return tournamentMapper.tournamentToTournamentDto(validTournamentDetails(tournament));
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

    @NotNull
    private TournamentEntity validTournamentDetails(TournamentEntity tournament) {
        if (startDateNotBeforeEndDate(tournament.getStartDate(), tournament.getEndDate())) {
            String dateExceptionMessage = "Start date must be before end date";
            throw new TournamentServiceException(dateExceptionMessage);
        }
        UUID eventId = tournament.getEvent() == null ? null : tournament.getEvent().getUuid();
        if (eventId != null && !eventRepository.existsById(eventId)) {
            throw new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(eventId));
        }
        return tournamentRepository.save(tournament);
    }
}
