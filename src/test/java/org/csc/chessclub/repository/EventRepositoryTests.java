package org.csc.chessclub.repository;

import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.model.EventEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventRepositoryTests extends TestContainerConfig {

    @Autowired
    private EventRepository eventRepository;
    private static final String TITLE_1 = "First test Event";
    private static final String TITLE_2 = "Second test Event";
    private static final String AUTHOR = "Test Author";

    private EventEntity event1;
    private EventEntity event2;

    @BeforeEach
    public void setUp() {
        String description = "Test Description";
        String announcementPDF = "Test Announcement PDF";
        LocalDate date = LocalDate.now();
         event1 = EventEntity
                .builder()
                .description(description)
                .announcementPDF(announcementPDF)
                .author(AUTHOR)
                .createdAt(date)
                .title(TITLE_1)
                .available(true)
                .build();

        event2 = EventEntity
                .builder()
                .description(description)
                .announcementPDF(announcementPDF)
                .author(AUTHOR)
                .createdAt(date)
                .title(TITLE_2)
                .build();
    }

    @Test
    @DisplayName("Find event by title")
    void testFindEventByTitle_whenGivenTitle_returnEventWithGivenTitle() {
        //TODO: result should have available true
        eventRepository.save(event1);
        List<EventEntity> retrievedEvent = eventRepository.findEventEntityByTitle(TITLE_1);

        assertNotNull(retrievedEvent, "Event should not be null");
        Assertions.assertEquals(1, retrievedEvent.size(),
                "List should contain only one element");
        assertEquals(TITLE_1, retrievedEvent.getFirst().getTitle(),
                "Title of Event should be equal");

    }

    @Test
    @DisplayName("Find events by author")
    void testFindEventByAuthor_whenGivenAuthor_returnEventWithGivenAuthor() {
        eventRepository.save(event2);
        List<EventEntity> retrievedEvents = eventRepository.findEventEntityByAuthor(AUTHOR);

        assertNotNull(retrievedEvents, "Event should not be null");
        Assertions.assertEquals(1, retrievedEvents.size(),
                "List should contain only one element");
        assertEquals(AUTHOR, retrievedEvents.getFirst().getAuthor(),
                "Author of Event should be equal");
    }

    //TODO: return only available entities (this should be default for user)
}

