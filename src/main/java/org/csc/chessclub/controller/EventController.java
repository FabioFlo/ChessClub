package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.dto.GetEventDto;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
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


}
