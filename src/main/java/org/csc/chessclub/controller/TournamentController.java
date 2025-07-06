package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.tournament.CreateTournamentDto;
import org.csc.chessclub.dto.tournament.TournamentDto;
import org.csc.chessclub.dto.tournament.UpdateTournamentDto;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.service.tournament.TournamentService;
import org.csc.chessclub.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Valid
public class TournamentController {
    private final TournamentService tournamentService;
    private final PageUtils<TournamentDto> pageUtils;

    private static final String CREATED = "Tournament successfully created";
    private static final String UPDATED = "Tournament successfully updated";
    private static final String FOUND = "Tournament found";
    private static final String LIST_FOUND = "Tournaments found";
    private static final String DELETED = "Tournament deleted";

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<TournamentDto>> createTournament(@RequestBody @Valid CreateTournamentDto createTournamentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(tournamentService.create(createTournamentDto), CREATED, true));
    }

    @PatchMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<TournamentDto>> updateTournament(@RequestBody @Valid UpdateTournamentDto updateTournamentDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(tournamentService.update(updateTournamentDto), UPDATED, true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<TournamentDto>> getTournament(@ValidUUID @PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(tournamentService.getById(uuid), FOUND, true));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageResponseDto<TournamentDto>>> getAllTournaments(@PageableDefault Pageable pageable) {
        Page<TournamentDto> pagedTournaments = tournamentService.getAllAvailable(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pagedTournaments), LIST_FOUND, true));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<UUID>> deleteTournament(@ValidUUID @PathVariable UUID uuid) {
        tournamentService.delete(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(uuid, DELETED, true));
    }

}
