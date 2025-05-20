package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
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
}
