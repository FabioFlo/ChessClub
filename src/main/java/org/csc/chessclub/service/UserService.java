package org.csc.chessclub.service;

import org.csc.chessclub.model.UserEntity;

import java.util.UUID;

public interface UserService {
    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user);

    UserEntity getById(UUID uuid);

    UserEntity delete(UUID uuid);

}
