package org.csc.chessclub.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.auth.AuthenticationRequest;
import org.csc.chessclub.auth.AuthenticationResponse;
import org.csc.chessclub.auth.AuthenticationService;
import org.csc.chessclub.dto.ResponseDto;
import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.exception.validation.uuid.ValidUUID;
import org.csc.chessclub.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final AuthenticationService authService;

  private static final String CREATED = "User successfully created";
  private static final String LOGGED_IN = "User successfully logged in";
  private static final String UPDATED = "User successfully updated";
  private static final String FOUND = "User found";
  private static final String DELETED = "User deleted";

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping()
  public ResponseEntity<ResponseDto<UserDto>> createUser(
      @Valid @RequestBody RegisterUserRequest userRequest) {
    UserDto userDto = userService.create(userRequest);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ResponseDto<>(userDto, CREATED, true));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDto<AuthenticationResponse>> login(
      @Valid @RequestBody AuthenticationRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseDto<>(authService.authenticate(request), LOGGED_IN, true));
  }

  @PreAuthorize("isAuthenticated")
  @PatchMapping()
  public ResponseEntity<ResponseDto<UserDto>> updateUser(
      @Valid @RequestBody UpdateUserRequest userRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseDto<>(userService.update(userRequest), UPDATED, true));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{uuid}")
  public ResponseEntity<ResponseDto<UserDto>> getUser(@ValidUUID @PathVariable UUID uuid) {
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
        userService.getById(uuid), FOUND, true));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{uuid}")
  public ResponseEntity<ResponseDto<UUID>> deleteUser(@ValidUUID @PathVariable UUID uuid) {
    userService.delete(uuid);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
        uuid, DELETED, true));
  }
}
