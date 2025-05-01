package org.csc.chessclub.service;

import lombok.extern.java.Log;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log
public class EventServiceUnitTest {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;

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
                .date(date)
                .title(title)
                .available(true)
                .build();
        log.info("setUp UUID: " + uuid);
    }

    @Test
    @DisplayName("Create Event")
    public void testCreateEvent_whenEventDetailsProvided_returnEvent() {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity createdEvent = eventService.create(event);

        assertNotNull(createdEvent, "Event should not be null");
        assertNotNull(createdEvent.getUuid(), "UUID should not be null");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Update Event")
    public void testUpdateEvent_whenEventDetailsProvided_returnUpdatedEvent() {
        when(eventRepository.existsById(event.getUuid())).thenReturn(true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        event.setTitle("Updated Title");

        EventEntity updatedEvent = eventService.update(event);

        assertNotNull(updatedEvent, "Event should not be null");
        assertEquals(event.getTitle(), updatedEvent.getTitle(), "Title of Event should be equal");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Get Event By Id")
    void testGetEvent_whenEventFoundById_returnEvent() {
        when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));

        EventEntity retrievedEvent = eventService.getById(event.getUuid());

        assertNotNull(retrievedEvent, "Event should not be null");
        assertEquals(event, retrievedEvent, "Event should be equal");
        verify(eventRepository, times(1)).findById(event.getUuid());
    }

    @Test
    @DisplayName("Get all events")
    void testGetAllEvents_whenEventsFound_returnAllEvents() {
        when(eventRepository.findAll()).thenReturn(java.util.List.of(event));

        List<EventEntity> allEvents = eventService.getAll();

        assertNotNull(allEvents);
        assertEquals(1, allEvents.size());
    }

    @Test
    @DisplayName("Delete event")
    void testDeleteEvent_whenEventFound_availableShouldBeSetToFalse() {
        when(eventRepository.existsById(event.getUuid())).thenReturn(true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        event.setAvailable(false);

        assertDoesNotThrow(() -> eventService.delete(event));
        assertFalse(event.isAvailable());
    }

    @Test
    @DisplayName("Throw when Event Not Found")
    void testEventService_whenEventNotFound_shouldThrowWhenEventNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.getById(event.getUuid());
        });

        assertTrue(exception.getMessage().contains("Event not found"));
    }
}
