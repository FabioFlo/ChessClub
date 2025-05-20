package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.UserServiceException;
import org.csc.chessclub.model.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity create(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findUserEntityByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserServiceException("Email or username already taken");
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findByUsernameOrEmailAndUuidNot(user.getUsername(), user.getEmail(), user.getUuid());
        if (existingUser.isPresent()) {
            throw new UserServiceException("Email or username already taken");
        }
        if (userRepository.existsById(user.getUuid())) {
            return userRepository.save(user);
        }
        throw new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(user.getUuid()));
    }
}
