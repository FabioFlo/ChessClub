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
    public ResponseEntity<String> createEvent(@Valid @RequestBody CreateEventDto createEventDto) {
        eventService.create(eventMapper.createEventDtoToEvent(createEventDto));

        return ResponseEntity.status(HttpStatus.CREATED).body("Event created");
    }

    @GetMapping()
    public ResponseEntity<List<GetEventDto>> getAllEvents() {
        return ResponseEntity.ok(eventMapper
                .eventEntityListToGetEventDtoList(eventService.getAll()));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<GetEventDto> getEventById(@ValidUUID @PathVariable UUID uuid) {
        return ResponseEntity.ok(
                eventMapper.eventToGetEventDto(eventService.getById(uuid)));
    }

    @PatchMapping()
    public ResponseEntity<EventDetailsDto> updateEvent(@Valid @RequestBody EventDetailsDto eventDetailsDto) {
        eventService.update(eventMapper.eventDetailsDtoToEvent(eventDetailsDto));
        return ResponseEntity.ok(eventDetailsDto);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ResponseDto<UUID>> deleteEvent(@ValidUUID @PathVariable UUID uuid) {
        eventService.delete(uuid);
        return ResponseEntity.ok(new ResponseDto<>(uuid,"Event deleted", true));
    }

}
