package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.EventServiceException;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public EventEntity create(EventEntity event) {
        return save(event);
    }

    @Override
    public EventEntity update(EventEntity event) {
        if (eventRepository.existsById(event.getUuid())) {
            return save(event);
        }
        throw new RuntimeException("Event not found with ID: " + event.getUuid());
    }

    @Override
    public EventEntity getById(UUID uuid) {
        return eventRepository.findById(uuid)
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(uuid)));
    }

    @Override
    public EventEntity delete(UUID uuid) {
        Optional<EventEntity> event = eventRepository.findById(uuid);
        if (event.isPresent()) {
            event.get().setAvailable(false);
            return eventRepository.save(event.get());
        }
        throw new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(uuid));
    }

    @Override
    public List<EventEntity> getAll() {
        return eventRepository.findAll();
    }

    EventEntity save(EventEntity event) {
        if (event.getTitle() == null || event.getTitle().isEmpty()) {
            throw new EventServiceException("Title cannot be null or empty");
        }
        if (event.getAuthor() == null || event.getAuthor().isEmpty()) {
            throw new EventServiceException("Author cannot be null or empty");
        }
        if (event.getDescription() == null || event.getDescription().isEmpty()) {
            throw new EventServiceException("Description cannot be null or empty");
        }
        return eventRepository.save(event);
    }
}

