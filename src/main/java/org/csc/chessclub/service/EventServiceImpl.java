package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.exception.EventServiceException;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public EventEntity create(EventEntity event) {
        if (event.getTitle() == null || event.getTitle().isEmpty()) {
            throw new EventServiceException("Title cannot be null or empty");
        }
        if (event.getAuthor() == null || event.getAuthor().isEmpty()) {
            throw new EventServiceException("Author cannot be null or empty");
        }
        return eventRepository.save(event);
    }

    @Override
    public EventEntity update(EventEntity event) {
        if (eventRepository.existsById(event.getUuid())) {
            return eventRepository.save(event);
        }
        throw new RuntimeException("Event not found with ID: " + event.getUuid());
    }

    @Override
    public EventEntity getById(UUID uuid) {
        return eventRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + uuid));
    }

    @Override
    public EventEntity delete(EventEntity event) {
        if (eventRepository.existsById(event.getUuid())) {
            event.setAvailable(false);
            return eventRepository.save(event);
        }
        throw new RuntimeException("Event not found with ID: " + event.getUuid());
    }

    @Override
    public List<EventEntity> getAll() {
        return eventRepository.findAll();
    }
}

