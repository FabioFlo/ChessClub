package org.csc.chessclub.service;

import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    private EventEntity event;

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;


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
                .date(date)
                .title(title)
                .build();
    }

    @Test
    @DisplayName("Create Event")
    public void testCreateEvent_whenEventDetailsProvided_returnEvent() {

        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity createdEvent = eventService.create(event);

        assertNotNull(createdEvent, "Event should not be null");
        assertNotNull(createdEvent.getUuid(), "UUID should not be null");

    }
}
