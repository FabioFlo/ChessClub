package org.csc.chessclub.service.user;

import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.UserServiceException;
import org.csc.chessclub.model.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceExceptionTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "<PASSWORD>";
    private static final String EMAIL = "email@email.com";
    private static final Role ROLE = Role.ADMIN;
    private static final UUID uuid = UUID.randomUUID();
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        new UserEntity();
        user = UserEntity
                .builder()
                .uuid(uuid)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .role(ROLE)
                .build();
    }


    @Test
    @DisplayName("Create User - Throw when User Already Exists")
    void testCreateUser_whenUserProvided_shouldThrowUserExceptionIfEmailOrUsernameAlreadyTaken() {
        when(userRepository.findUserEntityByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(Optional.ofNullable(user));

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.create(user));

        assertTrue(exception.getMessage().contains("Email or username already taken"));

    }

    @Test
    @DisplayName("Update User - Throw when User Already Exists with different Id")
    void testUpdateUser_whenUserProvided_shouldThrowUserExceptionIfEmailOrUsernameAlreadyTakenWithDifferentId() {
        when(userRepository.findByUsernameOrEmailAndUuidNot(USERNAME, EMAIL, user.getUuid())).thenReturn(Optional.ofNullable(user));

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.update(user));

        assertTrue(exception.getMessage().contains("Email or username already taken"));
    }

    @Test
    @DisplayName("Throw when User Not Found")
    void testUserService_whenUserNotFoundById_shouldThrowCustomNotFoundException() {
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> userService.update(user));

        assertTrue(exception.getMessage().contains(
                NotFoundMessage.USER_WITH_UUID.format((user.getUuid()))));
    }

}
