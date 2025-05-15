package org.csc.chessclub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.csc.chessclub.utils.ValidUUID;

import java.util.UUID;

public record EventDetailsDto(
        @ValidUUID(message = "UUID must be valid")
        UUID uuid,
        @NotBlank(message = "Title must not be blank")
        @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
        String title,
        @NotBlank(message = "Description must not be blank")
        String description,
        @NotBlank(message = "Author must not be blank")
        @Size(min = 2, max = 100, message = "Author must be between 2 and 100 characters")
        String author,
        String announcementPDF
) {
}
