package org.csc.chessclub.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateEventDto(
        @NotBlank(message = "Title must not be blank")
        String title,
        String description,
        String author,
        String announcementPDF
) {
}
