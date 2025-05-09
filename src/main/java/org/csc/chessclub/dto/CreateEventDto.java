package org.csc.chessclub.dto;

public record CreateEventDto(
        String title,
        String description,
        String author,
        String announcementPDF
) {
}
