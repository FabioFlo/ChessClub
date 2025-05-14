package org.csc.chessclub.service;

import org.csc.chessclub.exception.EventServiceException;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
                .createdAt(date)
                .title(title)
                .available(true)
                .build();
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
        when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        event.setAvailable(false);

        assertDoesNotThrow(() -> eventService.delete(event.getUuid()));
        assertFalse(event.isAvailable());
    }

    @Test
    @DisplayName("Throw when Event Not Found")
    void testEventService_whenEventNotFound_shouldThrowWhenEventNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.getById(event.getUuid()));

        assertTrue(exception.getMessage().contains("Event not found"));
    }

    @Test
    @DisplayName("Empty title throw EventServiceException")
    void testEventService_whenEmptyTitle_shouldThrowEventServiceException() {
        event.setTitle("");
        String aspectMessage = "Title cannot be null or empty";

        RuntimeException exception = assertThrows(EventServiceException.class, () -> eventService.create(event));

        assertTrue(exception.getMessage().contains(aspectMessage));
    }

    @Test
    @DisplayName("Empty author throw EventServiceException")
    void testEventService_whenEmptyAuthor_shouldThrowEventServiceException() {
        event.setAuthor("");
        String aspectMessage = "Author cannot be null or empty";

        RuntimeException exception = assertThrows(EventServiceException.class, () -> eventService.create(event));

        assertTrue(exception.getMessage().contains(aspectMessage));
    }

    @Test
    @DisplayName("Empty description throw EventServiceException")
    void testEventService_whenEmptyDescription_shouldThrowEventServiceException() {
        event.setDescription("");
        String aspectMessage = "Description cannot be null or empty";

        RuntimeException exception = assertThrows(EventServiceException.class, () -> eventService.create(event));

        assertTrue(exception.getMessage().contains(aspectMessage));
    }

    @Test
    @DisplayName("Save if valid Event details")
    void testSaveEvent_whenValidEventDetailsProvided_returnEvent() {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity savedEvent = eventService.save(event);

        assertTrue(event.getTitle() != null && !event.getTitle().isEmpty());
        assertTrue(event.getAuthor() != null && !event.getAuthor().isEmpty());
        assertTrue(event.getDescription() != null && !event.getDescription().isEmpty());
        assertNotNull(savedEvent);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }
}
