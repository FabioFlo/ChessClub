package org.csc.chessclub.service.event;

import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.service.storage.StorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Spy
    private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    private EventEntity event;
    private EventEntity availableEvent;
    private Pageable pageable;
    private CreateEventDto createEventDto;

    private final String description = "Test Description";
    private final String author = "Test Author";
    private final String title = "Test Event";

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 10);

        UUID uuid = UUID.randomUUID();


        LocalDate date = LocalDate.now();
        event = EventEntity
                .builder()
                .uuid(uuid)
                .description(description)
                .author(author)
                .createdAt(date)
                .title(title)
                .build();

        availableEvent = EventEntity.builder()
                .uuid(UUID.randomUUID())
                .description(description)
                .author(author)
                .createdAt(date)
                .title(title)
                .available(true)
                .build();

        createEventDto = new CreateEventDto(title, description, author);
    }

    @Test
    @DisplayName("Create Event")
    public void testCreateEvent_whenEventDetailsProvided_returnEvent() throws IOException {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventDto createdEvent = eventService.create(createEventDto, null);

        assertNotNull(createdEvent, "Event should not be null");
        assertNotNull(createdEvent.uuid(), "UUID should not be null");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Create Event with pdf")
    public void testCreateEvent_whenCreateEventDtoProvided_returnEvent() throws IOException {
        String filename = "announcement.pdf";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(eventRepository.save(any(EventEntity.class)))
                 .thenAnswer(invocation -> invocation.getArgument(0));
        when(storageService.store(mockFile)).thenReturn(filename);

        EventDto eventDto = eventService.create(createEventDto, mockFile);

        assertNotNull(eventDto, "Event should not be null");
        assertNotNull(eventDto.announcementPDF(), "Pdf name should be present");
        assertEquals(filename, eventDto.announcementPDF());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Update Event")
    public void testUpdateEvent_whenEventDetailsProvided_returnUpdatedEvent() throws IOException {
        String newTitle = "Updated Title";
        when(eventRepository.save(any(EventEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(eventRepository.findById(availableEvent.getUuid())).thenReturn(Optional.ofNullable(availableEvent));

        UpdateEventDto eventDto = new UpdateEventDto(availableEvent.getUuid(), newTitle, description, author);

        EventDto updatedEvent = eventService.update(eventDto, null);

        assertNotNull(updatedEvent, "Event should not be null");
        assertEquals(eventDto.title(), updatedEvent.title(), "Title of Event should be equal");
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should Update Event with pdf filename")
    public void testUpdateEvent_whenUpdateEventDtoProvidedWithPdf_returnUpdatedEvent() throws IOException {
        String filename = "announcement.pdf";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(storageService.store(mockFile)).thenReturn(filename);
        when(eventRepository.findById(event.getUuid())).thenReturn(Optional.ofNullable(event));
        when(eventRepository.save(any(EventEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateEventDto eventDto = new UpdateEventDto(event.getUuid(), title, description, author);

        EventDto updatedEvent = eventService.update(eventDto, mockFile);

        assertNotNull(updatedEvent, "Event should not be null");
        assertNotNull(updatedEvent.announcementPDF(), "Pdf name should be present");
        assertEquals(filename, updatedEvent.announcementPDF(), "Pdf name should be equal");
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
        List<EventEntity> allEvents = List.of(event);
        Page<EventEntity> pagedEvents = new PageImpl<>(allEvents, pageable, allEvents.size());

        when(eventRepository.findAll(any(Pageable.class))).thenReturn(pagedEvents);

        Page<EventEntity> result = eventService.getAll(pageable);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Get all events available")
    void testGetAllEvents_whenEventsFoundWithAvailableTrue_returnAllEvents() {
        List<EventEntity> allEvents = List.of(availableEvent);
        Page<EventEntity> pagedEvents = new PageImpl<>(allEvents, pageable, allEvents.size());

        when(eventRepository.findAllByAvailableTrue(any(Pageable.class))).thenReturn(pagedEvents);

        Page<EventEntity> result = eventService.getAllAvailable(pageable);

        assertNotNull(result,
                "Result should not be null");
        assertEquals(1, result.getTotalElements(),
                "Should return one event available");
    }

    @Test
    @DisplayName("Delete event")
    void testDeleteEvent_whenEventFound_availableShouldBeSetToFalse() {
        when(eventRepository.setAvailableFalse(event.getUuid())).thenReturn(1);

        assertDoesNotThrow(() -> eventService.delete(event.getUuid()));
    }

    @Test
    @DisplayName("Save if valid Event details")
    void testSaveEvent_whenValidEventDetailsProvided_returnEvent() throws IOException {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(event);

        EventDto savedEvent = eventService.create(createEventDto, null);

        assertTrue(event.getTitle() != null && !event.getTitle().isEmpty());
        assertTrue(event.getAuthor() != null && !event.getAuthor().isEmpty());
        assertTrue(event.getDescription() != null && !event.getDescription().isEmpty());
        assertNotNull(savedEvent);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

}
