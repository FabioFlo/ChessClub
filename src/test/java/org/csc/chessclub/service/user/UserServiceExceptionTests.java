package org.csc.chessclub.service.user;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.enums.NotFoundMessage;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.exception.CustomAccessDeniedException;
import org.csc.chessclub.exception.CustomNotFoundException;
import org.csc.chessclub.exception.UserServiceException;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Create User - Throw when User Already Exists")
    void testCreateUser_whenUserProvided_shouldThrowUserExceptionIfEmailOrUsernameAlreadyTaken() {
        RegisterUserRequest userRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

        when(userRepository.findUserEntityByUsernameOrEmail(USERNAME, EMAIL)).thenReturn(
                Optional.ofNullable(user));

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.create(userRequest));

        assertTrue(exception.getMessage().contains("Email or username already taken"));

    }

    @Test
    @DisplayName("Update User - Throw when User Already Exists with different Id")
    void testUpdateUser_whenUserProvided_shouldThrowUserExceptionIfEmailOrUsernameAlreadyTakenWithDifferentId() {

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(
                userRepository.findByUsernameOrEmailAndUuidNot(USERNAME, EMAIL, user.getUuid())).thenReturn(
                Optional.ofNullable(user));
        UpdateUserRequest updateUser = new UpdateUserRequest(uuid, USERNAME, EMAIL);

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> userService.update(updateUser));

        assertTrue(exception.getMessage().contains("Email or username already taken"));
    }

    @Test
    @DisplayName("Throw when User Not Found")
    void testUserService_whenUserNotFoundById_shouldThrowCustomNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UpdateUserRequest updateUser = new UpdateUserRequest(uuid, USERNAME, EMAIL);
        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> userService.update(updateUser));

        assertTrue(exception.getMessage().contains(
                NotFoundMessage.USER_WITH_UUID.format((user.getUuid()))));
    }

    @Test
    @DisplayName("Throw when not allowed to update user")
    void testUpdateUser_whenUserIdDoNotMatchAndIsNotAdmin_shouldThrowAccessDeniedException() {
        UUID userUUID = UUID.randomUUID();
        UUID notAllowedUserUUID = UUID.randomUUID();
        String newUsername = "new username";
        String newEmail = "newEmail@email.com";

        UserEntity notAllowedUser = new UserEntity();
        notAllowedUser.setUuid(notAllowedUserUUID);
        notAllowedUser.setRole(Role.USER);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(notAllowedUser);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UpdateUserRequest updateUser = new UpdateUserRequest(userUUID, newUsername, newEmail);

        assertThatThrownBy(() -> userService.update(updateUser))
                .isInstanceOf(CustomAccessDeniedException.class)
                .hasMessageContaining("You are not allowed to update this user");
    }
}
