package org.csc.chessclub.service;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.model.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity create(UserEntity user) {
        return userRepository.save(user);
    }
}
