package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.service.tournament.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Valid
public class TournamentController {
    private final TournamentService tournamentService;

    private static final String CREATED = "Tournament successfully created";

    @PostMapping
    public ResponseEntity<ResponseDto<TournamentDto>> createTournament(@RequestBody @Valid CreateTournamentDto createTournamentDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(tournamentService.create(createTournamentDto), CREATED, true));
    }
}
