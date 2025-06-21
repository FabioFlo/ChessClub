package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.PageResponseDto;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.game.CreateGameDto;
import org.csc.chessclub.dto.game.GameDto;
import org.csc.chessclub.dto.game.UpdateGameDto;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.mapper.GameMapper;
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
    private final GameMapper gameMapper;
    private final PageUtils<GameDto> pageUtils;

    @PostMapping
    public ResponseEntity<ResponseDto<GameDto>> createGame(@Valid @RequestBody CreateGameDto createGameDto) {
        GameDto gameDto = gameMapper.gameToGameDto(
                gameService.create(gameMapper.createGameDtoToGame(createGameDto)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(gameDto, "Game created", true));
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<GameDto>> updateGame(@Valid @RequestBody UpdateGameDto updateGameDto) {
        GameDto gameDto = gameMapper.gameToGameDto(
                gameService.update(gameMapper.updateGameDtoToGame(updateGameDto)));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(gameDto, "Game updated", true));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto<GameDto>> getGame(@ValidUUID @PathVariable UUID uuid) {
        GameDto gameDto = gameMapper.gameToGameDto(gameService.getByUuid(uuid));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(gameDto, "Game found", true));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageResponseDto<GameDto>>> getAllGames(@PageableDefault Pageable pageable) {
        Page<GameDto> pageResult = gameMapper.pageGameEntityToPageGameDto(gameService.getAll(pageable));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pageResult), "Game list", true));
    }

    @GetMapping("/player/{player-name}")
    public ResponseEntity<ResponseDto<PageResponseDto<GameDto>>> getAllByPlayerName(@PageableDefault Pageable pageable,
                                                                                    @PathVariable("player-name") String playerName) {
        Page<GameDto> pageResult = gameMapper.pageGameEntityToPageGameDto(gameService.getAllByPlayerName(playerName, pageable));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(pageUtils.populatePageResponseDto(pageResult), "Player games", true));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto<UUID>> deleteGame(@ValidUUID @PathVariable UUID uuid) {
        gameService.delete(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(uuid, "Game deleted", true));
    }
}


