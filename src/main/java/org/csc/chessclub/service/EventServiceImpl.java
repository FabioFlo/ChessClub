package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.repository.EventRepository;

@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public EventEntity create(EventEntity event) {
        return eventRepository.save(event);
    }
}
