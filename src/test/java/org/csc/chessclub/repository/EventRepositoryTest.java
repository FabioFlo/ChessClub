package org.csc.chessclub.repository;

import org.csc.chessclub.model.EventEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private EventRepository eventRepository;
    private static final String TITLE = "Test Event";

    @Test
    void connectionTest() {
        assertNotNull(postgresContainer, "Container should not be null");
        Assertions.assertTrue(postgresContainer.isRunning(), "Container should be running");
    }

    private EventEntity event;

    @BeforeEach
    public void setUp() {
        String description = "Test Description";
        String author = "Test Author";
        String announcementPDF = "Test Announcement PDF";
        LocalDate date = LocalDate.now();
        event = EventEntity
                .builder()
                .description(description)
                .announcementPDF(announcementPDF)
                .author(author)
                .date(date)
                .title(TITLE)
                .available(true)
                .build();

    }

    @Test
    @DisplayName("Find event by title")
    void testFindEventByTitle_whenGivenTitle_returnEventWithGivenTitle() {
        eventRepository.save(event);
        List<EventEntity> retrievedEvent = eventRepository.findEventEntityByTitle(TITLE);

        assertNotNull(retrievedEvent, "Event should not be null");
        Assertions.assertEquals(1, retrievedEvent.size(),
                "List should contain only one element");
        assertEquals(TITLE, retrievedEvent.getFirst().getTitle(),
                "Title of Event should be equal");
    }
}

