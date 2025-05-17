package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.EventDetailsDto;
import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.service.EventService;
import org.csc.chessclub.utils.ValidUUID;
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
    public ResponseEntity<ResponseDto<GetEventDto>> createEvent(@Valid @RequestBody CreateEventDto createEventDto) {
        GetEventDto createdEvent = eventMapper.eventToGetEventDto(
                eventService.create(eventMapper.createEventDtoToEvent(createEventDto)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(createdEvent, "Event created", true));
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<List<GetEventDto>>> getAllEvents() {
        return ResponseEntity.ok(new ResponseDto<>(eventMapper
                .eventEntityListToGetEventDtoList(eventService.getAll()), "Events found", true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<GetEventDto>> getEventById(@ValidUUID @PathVariable UUID uuid) {
            return ResponseEntity.ok(new ResponseDto<>(
                    eventMapper.eventToGetEventDto(eventService.getById(uuid)), "Event found", true));

    }

    @PatchMapping()
    public ResponseEntity<ResponseDto<EventDetailsDto>> updateEvent(@Valid @RequestBody EventDetailsDto eventDetailsDto) {
        eventService.update(eventMapper.eventDetailsDtoToEvent(eventDetailsDto));
        return ResponseEntity.ok(new ResponseDto<>(eventDetailsDto, "Event updated", true));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ResponseDto<UUID>> deleteEvent(@ValidUUID @PathVariable UUID uuid) {
        eventService.delete(uuid);
        return ResponseEntity.ok(new ResponseDto<>(uuid, "Event deleted", true));
    }

}
