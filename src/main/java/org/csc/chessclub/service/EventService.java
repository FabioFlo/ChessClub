package org.csc.chessclub.service;

import org.csc.chessclub.model.EventEntity;

import java.util.List;
import java.util.UUID;

public interface EventService {
    EventEntity create(EventEntity event);
    EventEntity update(EventEntity event);
    EventEntity getById(UUID uuid);
    EventEntity delete(UUID uuid);
    List<EventEntity> getAll();
}
