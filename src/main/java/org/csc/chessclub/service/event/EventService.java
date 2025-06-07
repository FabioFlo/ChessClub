package org.csc.chessclub.service.event;

import org.csc.chessclub.model.EventEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EventService {
    EventEntity create(EventEntity event, MultipartFile file) throws IOException;
    EventEntity update(EventEntity event, MultipartFile file) throws IOException;
    EventEntity getById(UUID uuid);
    EventEntity delete(UUID uuid);
    List<EventEntity> getAll();
}
