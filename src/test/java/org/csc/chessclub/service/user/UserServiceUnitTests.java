package org.csc.chessclub.service.user;

import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static final Role ROLE = Role.USER;
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
    @DisplayName("Create User")
    public void testCreateUser_whenUserProvided_returnUser() {
        String encodedPassword = "$2a$10$encodedPassword";

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(PASSWORD)).thenReturn(encodedPassword);

        UserEntity createdUser = userService.create(user);
        UserEntity savedUser = userCaptor.getValue();

        assertNotNull(createdUser, "User should not be null");
        assertNotNull(createdUser.getUuid(), "UUID should not be null");
        assertEquals(Role.USER, createdUser.getRole(), "Role should be equal");
        assertTrue(createdUser.isAvailable(), "User should be available");
        assertNotEquals(PASSWORD, savedUser.getPassword(), "Password should not be equal");
        assertEquals(encodedPassword, savedUser.getPassword(), "Encoded password mismatch");

        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Update user")
    void testUpdateUser_whenAuthenticatedUserAndValidUserProvided_returnUpdatedUser() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.existsById(user.getUuid())).thenReturn(true);
        when(userRepository.save(Mockito.any())).thenReturn(user);

        user.setUsername("Updated Username");

        UserEntity updatedUser = userService.update(user);

        assertNotNull(updatedUser,
                "User should not be null");
        assertEquals(user.getUsername(), updatedUser.getUsername(),
                "Username of User should be equal");
        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Admin can update user")
    void testAdminCanUpdateUser_whenAuthenticatedUserAndValidUserProvided_returnUpdatedUser() {
        UserEntity admin =  UserEntity
                .builder()
                .uuid(UUID.randomUUID())
                .username("admin")
                .password(PASSWORD)
                .email("admin@email.com")
                .role(Role.ADMIN)
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(admin);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.existsById(user.getUuid())).thenReturn(true);
        when(userRepository.save(Mockito.any())).thenReturn(user);

        user.setUsername("Updated Username");

        UserEntity updatedUser = userService.update(user);

        assertNotNull(updatedUser,
                "User should not be null");
        assertEquals(user.getUsername(), updatedUser.getUsername(),
                "Username of User should be equal");
        assertNotNull(updatedUser.getRole(), "Role should not be null");
        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Get User By Id")
    void testGetUser_whenUserFoundById_returnUser() {
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));
        UserEntity retrievedUser = userService.getById(user.getUuid());

        assertNotNull(retrievedUser, "User should not be null");
        assertEquals(user, retrievedUser, "User should be equal");
        verify(userRepository, times(1)).findById(user.getUuid());
    }

    @Test
    @DisplayName("Delete user by Id set available false")
    void testDeleteUser_whenUserFoundById_availableShouldBeSetToFalse() {
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        user.setAvailable(false);

        assertDoesNotThrow(() -> userService.delete(user.getUuid()));
        assertFalse(user.isAvailable());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}
