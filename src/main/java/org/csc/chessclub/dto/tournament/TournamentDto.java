package org.csc.chessclub.dto.tournament;

import java.time.LocalDate;
import java.util.UUID;

public record TournamentDto(
        UUID uuid,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String description
) {
}
