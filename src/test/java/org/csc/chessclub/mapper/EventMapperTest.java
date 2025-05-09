package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.model.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
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

    @Test
    @DisplayName("Map get event dto to event")
    void shouldMapGetEventDtoToEventCorrectly() {
        CreateEventDto eventDto = new CreateEventDto("Test Title", "Test Description", "Test Author", "Test Announcement PDF");
        EventEntity event = eventMapper.createEventDtoToEvent(eventDto);

        assertEquals(eventDto.title(), event.getTitle(),
                "Title should be equal");
        assertEquals(eventDto.description(), event.getDescription(),
                "Description should be equal");
        assertEquals(eventDto.author(), event.getAuthor(),
                "Author should be equal");
        assertEquals(eventDto.announcementPDF(), event.getAnnouncementPDF(),
                "Announcement PDF should be equal");
    }

    @Test
    @DisplayName("Map list of EventEntity to list of GetEventDto ")
    void shouldMapListOfEventEntityToListOfGetEventDtoCorrectly() {
        List<GetEventDto> eventDtoList = eventMapper.eventEntityListToGetEventDtoList(java.util.List.of(event));

        assertEquals(1, eventDtoList.size(),
                "List should contain only one element");
        assertEquals(event.getUuid(), eventDtoList.getFirst().uuid(),
                "UUID should be equal");
        assertEquals(event.getDescription(), eventDtoList.getFirst().description(),
                "Description should be equal");
        assertEquals(event.getAnnouncementPDF(), eventDtoList.getFirst().announcementPDF(),
                "Announcement PDF should be equal");
    }
}
