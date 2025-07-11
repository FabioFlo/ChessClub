package org.csc.chessclub.repository;

import jakarta.transaction.Transactional;
import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.model.event.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
    @DisplayName("Find event by title with available true")
    @Order(1)
    void testFindEventByTitle_whenGivenTitleAndAvailableIsTrue_returnEventWithGivenTitle() {
        eventRepository.save(event1);
        List<EventEntity> retrievedEvent = eventRepository.findEventEntityByTitleAndAvailableTrue(TITLE_1);

        assertAll("Find by title assertions",
                () -> assertNotNull(retrievedEvent,
                        "Event should not be null"),
                () -> assertTrue(retrievedEvent.getFirst().isAvailable(),
                        "Event should be available"),
                () -> assertEquals(1, retrievedEvent.size(),
                        "List should contain only one element"),
                () -> assertEquals(TITLE_1, retrievedEvent.getFirst().getTitle(),
                        "Title of Event should be equal"));

    }

    @Test
    @DisplayName("Find events by author with available true")
    @Order(2)
    void testFindEventByAuthor_whenGivenAuthor_returnEventWithGivenAuthor() {
        eventRepository.save(event2);
        List<EventEntity> retrievedEvents = eventRepository.findEventEntityByAuthorAndAvailableTrue(AUTHOR);

        assertAll("Find event by author assertions",
                () -> assertNotNull(retrievedEvents,
                        "Event should not be null"),
                () -> assertEquals(1, retrievedEvents.size(),
                        "List should contain only one element"),
                () -> assertTrue(retrievedEvents.getFirst().isAvailable(),
                        "Event should be available"),
                () -> assertEquals(AUTHOR, retrievedEvents.getFirst().getAuthor(),
                        "Author of Event should be equal"));
    }

    @Test
    @DisplayName("Find all available events paged")
    @Order(3)
    void testFindAllAvailableEvents_whenGivenPageable_returnEventPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventEntity> result = eventRepository.findAllByAvailableTrue(pageable);

        assertAll("Paged events assertions",
                () -> assertNotNull(result.getContent(),
                        "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(),
                        "One element should be found"),
                () -> assertTrue(result.stream().allMatch(EventEntity::isAvailable))
        );
    }

    @Test
    @DisplayName("Correctly set available to false")
    @Order(4)
    @Transactional
    void testSetAvailableFalse_whenGivenUuid_returnResult() {
        eventRepository.save(event1);
        int result = eventRepository.setAvailableFalse(event1.getUuid());

        assertEquals(1, result);
    }
}

