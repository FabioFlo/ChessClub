package org.csc.chessclub.dto;

import java.time.LocalDate;
import java.util.UUID;

public record GetEventDto(
        String title,
        String description,
        String author,
        String announcementPDF,
        LocalDate createdAt,
        UUID uuid,
        boolean available
) {
}
