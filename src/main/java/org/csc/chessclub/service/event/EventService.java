package org.csc.chessclub.service.event;

import java.io.IOException;
import java.util.UUID;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {

  EventDto create(CreateEventDto eventDto, MultipartFile file) throws IOException;

  EventDto update(UpdateEventDto event, MultipartFile file) throws IOException;

  EventDto getById(UUID uuid);

  void delete(UUID uuid);

  Page<EventDto> getAll(Pageable pageable);

  Page<EventDto> getAllAvailable(Pageable pageable);
}
