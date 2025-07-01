package org.csc.chessclub.repository;

import org.csc.chessclub.controller.TestContainerConfig;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTests extends TestContainerConfig {

    private UserEntity user1;
    private final String USERNAME1 = "Test Username";
    private final String EMAIL1 = "email1@email.com";

    private UserEntity user2;
    private final String USERNAME2 = "Test Username 2";
    private final String EMAIL2 = "email2@email.com";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        new UserEntity();
        String password1 = "password1";
        user1 = UserEntity
                .builder()
                .username(USERNAME1)
                .password(password1)
                .email(EMAIL1)
                .role(Role.USER)
                .build();

        String password2 = "<PASSWORD>";
        user2 = UserEntity
                .builder()
                .username(USERNAME2)
                .password(password2)
                .email(EMAIL2)
                .role(Role.ADMIN)
                .build();

    }

    @Test
    @DisplayName("Find user by username or email")
    void testFindUserByUsernameOrEmail_whenGivenUsernameOrEmail_returnUserWithGivenUsernameOrEmail() {
        userRepository.save(user1);
        Optional<UserEntity> retrievedUser = userRepository.findUserEntityByUsernameOrEmail(USERNAME1, EMAIL1);

        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(USERNAME1, retrievedUser.get().getUsername(), "Username should be equal");
        assertEquals(EMAIL1, retrievedUser.get().getEmail(), "Email should be equal");
    }

    @Test
    @DisplayName("Find user by username or email with different uuid")
    void testFindUserUsernameOrEmailWithDifferentUuid_whenGivenUsernameOrEmailAndUuidNot_returnUserWithGivenUsernameOrEmailAndIdNot() {
        userRepository.save(user2);
        Optional<UserEntity> retrievedUser = userRepository.findByUsernameOrEmailAndUuidNot(USERNAME2, EMAIL2, user1.getUuid());

        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(USERNAME2, retrievedUser.get().getUsername(),
                "Username should be equal");
        assertEquals(EMAIL2, retrievedUser.get().getEmail(),
                "Email should be equal");
    }
}
