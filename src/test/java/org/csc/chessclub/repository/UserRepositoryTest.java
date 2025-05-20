package org.csc.chessclub.repository;

import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer
            = new PostgreSQLContainer<>("postgres:latest");

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static final Role ROLE = Role.ADMIN;
    private UserEntity user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        new UserEntity();
        user = UserEntity
                .builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .role(ROLE)
                .build();
    }

    @Test
    @DisplayName("Find user by username or email")
    void testFindUserByUsernameOrEmail_whenGivenUsernameOrEmail_returnUserWithGivenUsernameOrEmail() {
        userRepository.save(user);
        Optional<UserEntity> retrievedUser = userRepository.findUserEntityByUsernameOrEmail(USERNAME, EMAIL);

        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(USERNAME, retrievedUser.get().getUsername(), "Username should be equal");
        assertEquals(EMAIL, retrievedUser.get().getEmail(), "Email should be equal");
    }

    @Test
    @DisplayName("Find user by username or email with different uuid")
    void testFindUserUsernameOrEmailWithDifferentUuid_whenGivenUsernameOrEmailAndUuidNot_returnUserWithGivenUsernameOrEmailAndIdNot() {
        userRepository.save(user);
        Optional<UserEntity> retrievedUser = userRepository.findByUsernameOrEmailAndUuidNot(USERNAME, EMAIL, user.getUuid());

        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(USERNAME, retrievedUser.get().getUsername(),
                "Username should be equal");
        assertEquals(EMAIL, retrievedUser.get().getEmail(),
                "Email should be equal");
    }
}
