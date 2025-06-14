package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.mapper.GameMapper;
import org.csc.chessclub.service.game.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Valid
@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;

    @PostMapping
    public ResponseEntity<ResponseDto<GameDto>> createGame(@Valid @RequestBody CreateGameDto createGameDto) {
        GameDto gameDto = gameMapper.gameToGameDto(
                gameService.create(gameMapper.createGameDtoToGame(createGameDto)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(gameDto, "Game created", true));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<UpdateGameDto>> updateGame(@Valid @RequestBody UpdateGameDto updateGameDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(updateGameDto, "Game updated", true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<GameDto>> getGame(@ValidUUID @PathVariable("uuid") UUID uuid) {
        GameDto gameDto = gameMapper.gameToGameDto(gameService.getByUuid(uuid));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(gameDto, "Game found", true));
    }
}


