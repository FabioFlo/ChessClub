package org.csc.chessclub.service;

import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventServiceExceptionTests {

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
                .build();
    }

    @Test
    @DisplayName("Get by id - Throw when Event Not Found")
    void testEventService_whenEventByIdNotFound_shouldThrowWhenEventNotFound() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> eventService.getById(event.getUuid()));

        assertEquals(NotFoundMessage.EVENT_WITH_UUID.format(event.getUuid()), exception.getMessage());

        verify(eventRepository, times(1)).findById(event.getUuid());
    }

    @Test
    @DisplayName("Delete - Throw when Event Not Found")
    void testEventService_whenTryToDeleteEvent_shouldThrowWhenEventNotFound() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> eventService.delete(event.getUuid()));

        assertTrue(exception.getMessage().contains(NotFoundMessage.EVENT_WITH_UUID.format(event.getUuid())));
    }

    @Test
    @DisplayName("Update - Throw when Event Not Found")
    void testEventService_whenTryToUpdateEvent_shouldThrowWhenEventNotFound() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> eventService.update(event));

        assertTrue(exception.getMessage().contains(NotFoundMessage.EVENT_WITH_UUID.format(event.getUuid())));
    }


}
