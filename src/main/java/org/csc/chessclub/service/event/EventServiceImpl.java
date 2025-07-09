package org.csc.chessclub.service.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.service.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final StorageService storageService;
    private final EventMapper eventMapper;

    @Override
    public EventDto create(CreateEventDto eventDto, MultipartFile file) throws IOException {
        String filename = null;
        if (file != null && !file.isEmpty()) {
            filename = storageService.store(file);
        }
        EventEntity event = eventMapper.createEventDtoToEvent(eventDto);
        event.setAnnouncementPDF(filename);
        event.setAvailable(true);
        return eventMapper.eventToEventDto(eventRepository.save(event));
    }

    @Override
    public EventDto update(UpdateEventDto eventDto, MultipartFile file) throws IOException {
        EventEntity eventEntity = eventRepository.findById(eventDto.uuid())
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(eventDto.uuid())));

        eventMapper.updateEventDtoToEvent(eventDto, eventEntity);

        String filename = null;
        if (file != null && !file.isEmpty()) {
            filename = storageService.store(file);
        }
        eventEntity.setAnnouncementPDF(filename);

        return eventMapper.eventToEventDto(eventRepository.save(eventEntity));
    }

    @Override
    public EventDto getById(UUID uuid) {
        EventEntity event = eventRepository.findById(uuid)
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(uuid)));
        return eventMapper.eventToEventDto(event);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        int result = eventRepository.setAvailableFalse(uuid);

        if (result == 0) {
            throw new CustomNotFoundException(NotFoundMessage.EVENT_WITH_UUID.format(uuid));
        }
    }

    @Override
    public Page<EventDto> getAll(Pageable pageable) {
        return eventMapper.pageEventEntityToPageEventDto(eventRepository.findAll(pageable));
    }

    @Override
    public Page<EventDto> getAllAvailable(Pageable pageable) {
        return eventMapper.pageEventEntityToPageEventDto(eventRepository.findAllByAvailableTrue(pageable));
    }
}

