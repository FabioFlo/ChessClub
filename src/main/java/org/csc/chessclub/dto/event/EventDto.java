package org.csc.chessclub.dto.event;

import java.time.LocalDate;
import java.util.UUID;

public record EventDto(
    UUID uuid,
    String title,
    String description,
    String author,
    String announcementPDF,
    LocalDate createdAt
) {

}
