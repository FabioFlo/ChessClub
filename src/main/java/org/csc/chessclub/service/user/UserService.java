package org.csc.chessclub.service.user;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.model.user.UserEntity;

import java.util.UUID;

public interface UserService {
    UserDto create(RegisterUserRequest request);

    UserDto update(UpdateUserRequest userRequest);

    UserEntity getById(UUID uuid);

    void delete(UUID uuid);

}
