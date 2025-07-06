package org.csc.chessclub.service.user;

import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.CustomAccessDeniedException;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.UserServiceException;
import org.csc.chessclub.mapper.UserMapper;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserEntity create(UserEntity user) {
        Optional<UserEntity> existingUser = userRepository.findUserEntityByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserServiceException("Email or username already taken");
        }
        user.setAvailable(true);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public UserDto update(UpdateUserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (!currentUser.getUuid().equals(userRequest.uuid()) &&
                !currentUser.getRole().equals(Role.ADMIN)) {
            throw new CustomAccessDeniedException("You are not allowed to update this user");
        }

        Optional<UserEntity> existingUser = userRepository
                .findByUsernameOrEmailAndUuidNot(userRequest.username(), userRequest.email(), userRequest.uuid());
        if (existingUser.isPresent()) {
            throw new UserServiceException("Email or username already taken");
        }
        UserEntity user = userRepository.findById(userRequest.uuid())
                .orElseThrow(() -> new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(userRequest.uuid())));

        userMapper.updateUserRequestToUser(userRequest, user);

        return userMapper.userToUserDto(userRepository.save(user));

    }

    @Override
    public UserEntity getById(UUID uuid) {
        Optional<UserEntity> user = userRepository.findById(uuid);
        return user.orElseThrow(
                () -> new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(uuid)));
    }

    @Override
    public UserEntity delete(UUID uuid) {
        Optional<UserEntity> user = userRepository.findById(uuid);
        if (user.isPresent()) {
            user.get().setAvailable(false);
            return userRepository.save(user.get());
        }
        throw new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(uuid));
    }
}
