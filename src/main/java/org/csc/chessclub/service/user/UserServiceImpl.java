package org.csc.chessclub.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.csc.chessclub.dto.user.RegisterUserRequest;
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
  public UserDto create(RegisterUserRequest request) {
    Optional<UserEntity> existingUser = userRepository.findUserEntityByUsernameOrEmail(
        request.username(), request.email());
    if (existingUser.isPresent()) {
      throw new UserServiceException("Email or username already taken");
    }
    UserEntity user = userMapper.registerUserRequestToUser(request);
    user.setAvailable(true);
    user.setRole(Role.USER);
    user.setPassword(passwordEncoder.encode(request.password()));

    return userMapper.userToUserDto(userRepository.save(user));
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
        .findByUsernameOrEmailAndUuidNot(userRequest.username(), userRequest.email(),
            userRequest.uuid());
    if (existingUser.isPresent()) {
      throw new UserServiceException("Email or username already taken");
    }
    UserEntity user = userRepository.findById(userRequest.uuid())
        .orElseThrow(() -> new CustomNotFoundException(
            NotFoundMessage.USER_WITH_UUID.format(userRequest.uuid())));

    userMapper.updateUserRequestToUser(userRequest, user);

    return userMapper.userToUserDto(userRepository.save(user));

  }

  @Override
  public UserDto getById(UUID uuid) {
    return userMapper.userToUserDto(userRepository.findById(uuid)
        .orElseThrow(
            () -> new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(uuid))));
  }

  @Override
  @Transactional
  public void delete(UUID uuid) {
    int result = userRepository.setAvailableFalse(uuid);
    if (result == 0) {
      throw new CustomNotFoundException(NotFoundMessage.USER_WITH_UUID.format(uuid));
    }

  }
}
