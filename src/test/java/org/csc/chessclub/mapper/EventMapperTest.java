package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.model.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventMapperTest {
    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private EventEntity event;

    @BeforeEach
    public void setUp() {
        UUID uuid = UUID.randomUUID();
        String title = "Test Event";
        String description = "Test Description";
        String author = "Test Author";
        String announcementPDF = "Test Announcement PDF";
        LocalDate date = LocalDate.now();
        event = EventEntity
                .builder()
                .uuid(uuid)
                .description(description)
                .announcementPDF(announcementPDF)
                .author(author)
                .createdAt(date)
                .title(title)
                .available(true)
                .build();
    }

    @Test
    @DisplayName("Map event to get event dto")
    void shouldMapEventToGetEventDtoCorrectly() {
        GetEventDto eventDto = eventMapper.eventToGetEventDto(event);
        assertEquals(event.getUuid(), eventDto.uuid(),
                "UUID should be equal");
        assertEquals(event.getDescription(), eventDto.description(),
                "Description should be equal");
        assertEquals(event.getAnnouncementPDF(), eventDto.announcementPDF(),
                "Announcement PDF should be equal");
        assertEquals(event.getAuthor(), eventDto.author(),
                "Author should be equal");
        assertEquals(event.getCreatedAt(), eventDto.createdAt(),
                "Created At should be equal");
        assertEquals(event.getTitle(), eventDto.title(),
                "Title should be equal");
        assertEquals(event.isAvailable(), eventDto.available(),
                "Available should be equal");
    }

}
