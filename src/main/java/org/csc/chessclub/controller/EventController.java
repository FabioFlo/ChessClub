package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.exception.validation.file.ValidFile;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.service.event.EventService;
import org.csc.chessclub.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final PageUtils<EventDto> pageUtils;

    private static final String CREATED = "Event successfully created";
    private static final String UPDATED = "Event successfully updated";
    private static final String FOUND = "Event found";
    private static final String LIST_FOUND = "Events found";
    private static final String DELETED = "Event deleted";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<EventDto>> createEvent(
            @Valid @RequestPart(value = "event") CreateEventDto createEventDto,
            @RequestPart(value = "pdfFile", required = false) MultipartFile file) throws IOException {

        EventDto createdEvent = eventService.create(createEventDto, file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(createdEvent, CREATED, true));
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<PageResponseDto<EventDto>>> getAllEvents(@PageableDefault Pageable pageable) {
        Page<EventDto> pagedEvent = eventService.getAllAvailable(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pagedEvent), LIST_FOUND, true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<EventDto>> getEventById(@ValidUUID @PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(eventService.getById(uuid), FOUND, true));
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<EventDto>> updateEvent(
            @Valid @RequestPart(value = "event") UpdateEventDto updateEventDto,
            @RequestPart(value = "file", required = false) @ValidFile MultipartFile file) throws IOException {

        EventDto eventDto = eventService.update(updateEventDto, file);

        return ResponseEntity.ok(new ResponseDto<>(eventDto, UPDATED, true));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<UUID>> deleteEvent(@ValidUUID @PathVariable UUID uuid) {
        eventService.delete(uuid);
        return ResponseEntity.ok(new ResponseDto<>(uuid, DELETED, true));
    }

}
