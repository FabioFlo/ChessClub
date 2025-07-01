package org.csc.chessclub.service.event;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.service.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final StorageService storageService;

    @Override
    public EventEntity create(EventEntity event, MultipartFile file) throws IOException {
        String filename = null;
        if (file != null && !file.isEmpty()) {
            filename = storageService.store(file);
        }
        event.setAnnouncementPDF(filename);
        event.setAvailable(true);
        return eventRepository.save(event);
    }

    @Override
    public EventEntity update(EventEntity event, MultipartFile file) throws IOException {
        if (!eventRepository.existsById(event.getUuid())) {
            throw new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(event.getUuid()));
        }
        String filename = null;
        if (file != null && !file.isEmpty()) {
            filename = storageService.store(file);
        }
        event.setAnnouncementPDF(filename);
        return eventRepository.save(event);
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
    public Page<EventEntity> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Page<EventEntity> getAllAvailable(Pageable pageable) {
        return eventRepository.getDistinctByAvailableTrue(pageable);
    }
}

