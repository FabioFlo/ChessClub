package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.service.tournament.TournamentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Valid
public class TournamentController {
    private final TournamentService tournamentService;

    private static final String CREATED = "Tournament successfully created";
    private static final String UPDATED = "Tournament successfully updated";

    @PostMapping
    public ResponseEntity<ResponseDto<TournamentDto>> createTournament(@RequestBody @Valid CreateTournamentDto createTournamentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(tournamentService.create(createTournamentDto), CREATED, true));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<TournamentDto>> updateTournament(@RequestBody @Valid UpdateTournamentDto updateTournamentDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(tournamentService.update(updateTournamentDto), UPDATED, true));
    }
}
