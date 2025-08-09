package org.csc.chessclub.service.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.csc.chessclub.dto.user.*;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.mapper.UserMapper;
import org.csc.chessclub.model.user.UserEntity;
import org.csc.chessclub.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static final Role ROLE = Role.USER;
    private static final UUID uuid = UUID.randomUUID();
    private UserEntity user;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = PageRequest.of(0, 10);

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
        RegisterUserRequest userRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepository.save(userCaptor.capture()))
                .thenAnswer(invocation -> {
                    UserEntity user = invocation.getArgument(0);
                    user.setUuid(UUID.randomUUID());
                    return user;
                });
        when(passwordEncoder.encode(PASSWORD)).thenReturn(encodedPassword);

        UserDto createdUser = userService.create(userRequest);
        UserEntity savedUser = userCaptor.getValue();

        assertNotNull(createdUser, "User should not be null");
        assertNotNull(savedUser.getUuid(), "UUID should not be null");
        assertEquals(Role.USER, createdUser.role(), "Role should be equal");
        assertTrue(savedUser.isAvailable(), "User should be available");
        assertNotEquals(PASSWORD, savedUser.getPassword(), "Password should not be equal");
        assertEquals(encodedPassword, savedUser.getPassword(), "Encoded password mismatch");

        verify(userRepository).save(Mockito.any());
    }

    @Test
    @DisplayName("Update user")
    void testUpdateUser_whenAuthenticatedUserAndValidUserProvided_returnUpdatedUser() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String newUsername = "Updated Username";
        UpdateUserRequest userRequest = new UpdateUserRequest(user.getUuid(), newUsername,
                user.getEmail());
        when(userRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.ofNullable(user));

        UserDto updatedUser = userService.update(userRequest);

        assertNotNull(updatedUser,
                "User should not be null");
        assertEquals(user.getUsername(), updatedUser.username(),
                "Username of User should be equal");
        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Admin can update user")
    void testAdminCanUpdateUser_whenAuthenticatedUserAndValidUserProvided_returnUpdatedUser() {
        UserEntity admin = UserEntity
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

        String newUsername = "Updated Username";
        UpdateUserRequest userRequest = new UpdateUserRequest(user.getUuid(), newUsername,
                user.getEmail());
        when(userRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.ofNullable(user));

        UserDto updatedUser = userService.update(userRequest);

        assertNotNull(updatedUser,
                "User should not be null");
        assertEquals(user.getUsername(), updatedUser.username(),
                "Username of User should be equal");
        assertNotNull(updatedUser.role(), "Role should not be null");
        verify(userRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Get User By Id")
    void testGetUser_whenUserFoundById_returnUser() {
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));
        UserDto userDto = userService.getById(user.getUuid());

        assertNotNull(userDto, "User should not be null");
        assertEquals(user.getUuid(), userDto.uuid(), "User should be equal");
        verify(userRepository, times(1)).findById(user.getUuid());
    }

    @Test
    @DisplayName("Get all paginated users")
    void testGetAllPaginatedUsers_whenUsersFound_thenReturnPaginatedUsers() {
        List<UserEntity> users = List.of(user);
        Page<UserEntity> pagedUsers = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(pagedUsers);

        Page<UserDto> result = userService.getAll(pageable);

        assertAll("Get all assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(), "Result should contain two users"));
    }

    @Test
    @DisplayName("Delete user by Id set available false")
    void testDeleteUser_whenUserFoundById_availableShouldBeSetToFalse() {
        when(userRepository.setAvailableFalse(user.getUuid())).thenReturn(1);

        assertDoesNotThrow(() -> userService.delete(user.getUuid()));
    }

    @Test
    @DisplayName("Update user role")
    void testUpdateUserRole_whenUpdateRoleDtoProvided_returnUpdatedUserRole() {
        Role oldRole = Role.USER;
        Role newRole = Role.ADMIN;
        UpdateRoleDto updateRole = new UpdateRoleDto(user.getUuid(), oldRole, newRole);

        when(userRepository.updateRole(user.getUuid(), oldRole, newRole)).thenReturn(1);

        Role result = userService.updateUserRole(updateRole);

        assertAll("Update role assertions",
                () -> assertNotNull(result, "Role should not be null"),
                () -> assertEquals(newRole, result, "Role should be equal"));
    }

    @Test
    @DisplayName("Update user password")
    void testUpdateUserPassword_whenUpdatePasswordDtoProvided_AndUserExists_UpdatePassword() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto(uuid, "New Password");

        when(userRepository.updatePassword(user.getUuid(), passwordEncoder.encode("New Password"))).thenReturn(1);

        assertDoesNotThrow(() -> userService.updateUserPassword(updatePasswordDto), "User password should be updated");
    }
}
