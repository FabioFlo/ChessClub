package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.model.event.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventMapperTests {

    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private EventEntity event;

    private final UUID uuid = UUID.randomUUID();
    private final LocalDate CREATED_AT = LocalDate.now();
    private final String DESCRIPTION = "Test Description";
    private final String AUTHOR = "Test Author";
    private final String TITLE = "Test Title";

    @BeforeEach
    public void setUp() {
        boolean available = true;
        event = EventEntity
                .builder()
                .uuid(uuid)
                .description(DESCRIPTION)
                .author(AUTHOR)
                .createdAt(CREATED_AT)
                .title(TITLE)
                .available(available)
                .build();
    }

    @Test
    @DisplayName("Map event to event dto")
    void shouldMapEventToEventDtoCorrectly() {
        EventDto eventDto = eventMapper.eventToEventDto(event);

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
    }

    @Test
    @DisplayName("Map CreateEventDto to EventEntity")
    void shouldMapCreateEventDtoToEventCorrectly() {
        CreateEventDto eventDto = new CreateEventDto(TITLE, DESCRIPTION, AUTHOR);
        EventEntity event = eventMapper.createEventDtoToEvent(eventDto);

        assertEquals(eventDto.title(), event.getTitle(),
                "Title should be equal");
        assertEquals(eventDto.description(), event.getDescription(),
                "Description should be equal");
        assertEquals(eventDto.author(), event.getAuthor(),
                "Author should be equal");

    }

    @Test
    @DisplayName("Map list of EventEntity to list of EventDto ")
    void shouldMapListOfEventEntityToListOfEventDtoCorrectly() {
        List<EventDto> eventDtoList = eventMapper.eventEntityListToEventDtoList(
                java.util.List.of(event));

        assertEquals(1, eventDtoList.size(),
                "List should contain only one element");
        assertEquals(event.getUuid(), eventDtoList.getFirst().uuid(),
                "UUID should be equal");
        assertEquals(event.getDescription(), eventDtoList.getFirst().description(),
                "Description should be equal");
        assertEquals(event.getAnnouncementPDF(), eventDtoList.getFirst().announcementPDF(),
                "Announcement PDF should be equal");
    }

    @Test
    @DisplayName("Map UpdateEventDto to EventEntity")
    void shouldMapUpdateEventDtoToEventEntityCorrectly() {
        String updatedTitle = "Updated Title";
        EventEntity updatedEvent = eventMapper.updateEventDtoToEvent(new UpdateEventDto(uuid,
                updatedTitle, DESCRIPTION, AUTHOR), event);

        assertEquals(uuid, updatedEvent.getUuid());
        assertEquals(updatedTitle, updatedEvent.getTitle());
        assertEquals(DESCRIPTION, updatedEvent.getDescription());
        assertEquals(AUTHOR, updatedEvent.getAuthor());
    }

    @Test
    @DisplayName("Map page of EventEntity to EventDto")
    void testMapPageOfEventEntityToPageOfEventDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<EventEntity> listTournaments = List.of(event);
        Page<EventEntity> pageOfTournament = new PageImpl<>(listTournaments, pageable,
                listTournaments.size());

        Page<EventDto> result = eventMapper.pageEventEntityToPageEventDto(pageOfTournament);

        assertAll("Page entity to page dto assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(), "Should be one tournament"),
                () -> assertEquals(0, result.getNumber(), "Page number should be equal to zero"),
                () -> assertEquals(event.getUuid(), result.getContent().getFirst().uuid(),
                        "Tournament UUID should be equal"));
    }
}
