package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.CreateEventDto;
import org.csc.chessclub.mapper.EventMapper;
import org.csc.chessclub.model.EventEntity;
import org.csc.chessclub.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping()
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestBody CreateEventDto createEventDto) {
        return new ResponseEntity<>(
                eventService.create(
                        eventMapper.createEventDtoToEvent(createEventDto)),
                HttpStatus.CREATED);
    }


}
