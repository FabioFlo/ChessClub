package org.csc.chessclub.service.event;

import org.csc.chessclub.model.event.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface EventService {
    EventEntity create(EventEntity event, MultipartFile file) throws IOException;
    EventEntity update(EventEntity event, MultipartFile file) throws IOException;
    EventEntity getById(UUID uuid);
    EventEntity delete(UUID uuid);
    Page<EventEntity> getAll(Pageable pageable);
}
