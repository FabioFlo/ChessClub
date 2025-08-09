package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.service.game.GameService;
import org.csc.chessclub.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Valid
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final PageUtils<GameDto> pageUtils;

    private static final String CREATED = "Game successfully created";
    private static final String UPDATED = "Game successfully updated";
    private static final String FOUND = "Game found";
    private static final String LIST_FOUND = "Games found";
    private static final String DELETED = "Game deleted";

    @PostMapping
    public ResponseEntity<ResponseDto<GameDto>> createGame(
            @Valid @RequestBody CreateGameDto createGameDto) {
        GameDto gameDto = gameService.create(createGameDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(gameDto, CREATED, true));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<GameDto>> updateGame(
            @Valid @RequestBody UpdateGameDto updateGameDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(gameService.update(updateGameDto), UPDATED, true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<GameDto>> getGame(@ValidUUID @PathVariable UUID uuid) {
        GameDto gameDto = gameService.getByUuid(uuid);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(gameDto, FOUND, true));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageResponseDto<GameDto>>> getAllGames(
            @PageableDefault Pageable pageable) {
        Page<GameDto> pageResult = gameService.getAllAvailable(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pageResult), LIST_FOUND, true));
    }

    @GetMapping("/player/{player-name}")
    public ResponseEntity<ResponseDto<PageResponseDto<GameDto>>> getAllByPlayerName(
            @PageableDefault Pageable pageable,
            @PathVariable("player-name") String playerName) {
        Page<GameDto> pageResult = gameService.getAllByPlayerName(playerName, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pageResult), LIST_FOUND, true));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<UUID>> deleteGame(@ValidUUID @PathVariable UUID uuid) {
        gameService.delete(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(uuid, DELETED, true));
    }

    @GetMapping("?tournamentUuid={tournamentUuid}")
    public ResponseEntity<ResponseDto<PageResponseDto<GameDto>>> getAllGamesByTournamentUuid(
            @ValidUUID @PathVariable("tournamentUuid") UUID tournamentUuid,
            @PageableDefault Pageable pageable) {
        Page<GameDto> pageResult = gameService.getAllByTournamentUuid(tournamentUuid, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pageResult), LIST_FOUND, true));
    }
}


