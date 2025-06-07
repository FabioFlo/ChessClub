package org.csc.chessclub.service.event;

import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.service.storage.StorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTests {

    @InjectMocks
    private EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private StorageServiceImpl storageService;

    private EventEntity event;

    @BeforeEach
    public void setUp() {
        UUID uuid = UUID.randomUUID();
        String title = "Test Event";
        String description = "Test Description";
        String author = "Test Author";
        LocalDate date = LocalDate.now();
        event = EventEntity
                .builder()
                .uuid(uuid)
                .description(description)
                .author(author)
                .createdAt(date)
                .title(title)
                .build();
    }

    @Test
    @DisplayName("Create Event")
    public void testCreateEvent_whenEventDetailsProvided_returnEvent() throws IOException {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity createdEvent = eventService.create(event, null);

        assertNotNull(createdEvent, "Event should not be null");
        assertNotNull(createdEvent.getUuid(), "UUID should not be null");
        assertTrue(createdEvent.isAvailable(), "Event should be available");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Create Event with pdf")
    public void testCreateEvent_whenCreateEventDtoProvided_returnEvent() throws IOException {
        String filename = "announcement.pdf";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(storageService.store(mockFile)).thenReturn(filename);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity createdEvent = eventService.create(event, mockFile);

        assertNotNull(createdEvent, "Event should not be null");
        assertNotNull(createdEvent.getAnnouncementPDF(), "Pdf name should be present");
        assertEquals(filename, createdEvent.getAnnouncementPDF());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Update Event")
    public void testUpdateEvent_whenEventDetailsProvided_returnUpdatedEvent() throws IOException {
        when(eventRepository.existsById(event.getUuid())).thenReturn(true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        event.setTitle("Updated Title");

        EventEntity updatedEvent = eventService.update(event, null);

        assertNotNull(updatedEvent, "Event should not be null");
        assertEquals(event.getTitle(), updatedEvent.getTitle(), "Title of Event should be equal");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should Update Event with pdf filename")
    public void testUpdateEvent_whenUpdateEventDtoProvidedWithPdf_returnUpdatedEvent() throws IOException {
        String filename = "announcement.pdf";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(storageService.store(mockFile)).thenReturn(filename);
        when(eventRepository.existsById(event.getUuid())).thenReturn(true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity updatedEvent = eventService.update(event, mockFile);

        assertNotNull(updatedEvent, "Event should not be null");
        assertNotNull(updatedEvent.getAnnouncementPDF(), "Pdf name should be present");
        assertEquals(filename, updatedEvent.getAnnouncementPDF());
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
    @DisplayName("Save if valid Event details")
    void testSaveEvent_whenValidEventDetailsProvided_returnEvent() throws IOException {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventEntity savedEvent = eventService.create(event, null);

        assertTrue(event.getTitle() != null && !event.getTitle().isEmpty());
        assertTrue(event.getAuthor() != null && !event.getAuthor().isEmpty());
        assertTrue(event.getDescription() != null && !event.getDescription().isEmpty());
        assertNotNull(savedEvent);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

}
