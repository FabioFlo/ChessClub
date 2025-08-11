package org.csc.chessclub.controller;

import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@TestConfiguration
public class TestDataFactory {
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public UserEntity createUser(UserEntity user) {
    return userRepository.save(
        UserEntity.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(passwordEncoder.encode(user.getPassword()))
            .role(user.getRole())
            .available(user.isAvailable())
            .build());
  }

  public UserEntity createUnavailableUser() {
    return createUser(
        UserEntity.builder()
            .username("unavailable")
            .email("unabailable@email.com")
            .password("Test123_")
            .role(Role.USER)
            .available(false)
            .build());
  }
}
