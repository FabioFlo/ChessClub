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
import org.csc.chessclub.mapper.UserMapper;
import org.csc.chessclub.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ResponseDto<UserDto>> createUser(@Valid @RequestBody RegisterUserRequest userRequest) {
        UserDto userDto = userMapper.userToUserDto(
                userService.create(userMapper.registerUserRequestToUser(userRequest)));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(userDto, "User registered", true));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<AuthenticationResponse>> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(authService.authenticate(request), "User logged in", true));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping()
    public ResponseEntity<ResponseDto<UpdateUserRequest>> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        userService.update(userMapper.updateUserRequestToUser(userRequest));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto<>(userRequest, "User updated", true));
    }
}
