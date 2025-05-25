package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.event.CreateEventDto;
import org.csc.chessclub.dto.event.EventDto;
import org.csc.chessclub.dto.event.UpdateEventDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.service.EventService;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping()
    public ResponseEntity<ResponseDto<EventDto>> createEvent(@Valid @RequestBody CreateEventDto createEventDto) {
        EventDto createdEvent = eventMapper.eventToGetEventDto(
                eventService.create(eventMapper.createEventDtoToEvent(createEventDto)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(createdEvent, "Event created", true));
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<List<EventDto>>> getAllEvents() {
        return ResponseEntity.ok(new ResponseDto<>(eventMapper
                .eventEntityListToGetEventDtoList(eventService.getAll()), "Events found", true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<EventDto>> getEventById(@ValidUUID @PathVariable UUID uuid) {
            return ResponseEntity.ok(new ResponseDto<>(
                    eventMapper.eventToGetEventDto(eventService.getById(uuid)), "Event found", true));

    }

    @PatchMapping()
    public ResponseEntity<ResponseDto<UpdateEventDto>> updateEvent(@Valid @RequestBody UpdateEventDto updateEventDto) {
        eventService.update(eventMapper.eventDetailsDtoToEvent(updateEventDto));
        return ResponseEntity.ok(new ResponseDto<>(updateEventDto, "Event updated", true));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ResponseDto<UUID>> deleteEvent(@ValidUUID @PathVariable UUID uuid) {
        eventService.delete(uuid);
        return ResponseEntity.ok(new ResponseDto<>(uuid, "Event deleted", true));
    }

}
