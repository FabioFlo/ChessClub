package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMapperTests {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    private static final String USERNAME = "Test Username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static final Role ROLE = Role.USER;
    private static final UUID uuid = UUID.randomUUID();
    private static final boolean AVAILABLE = true;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = UserEntity
                .builder()
                .uuid(uuid)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .role(ROLE)
                .available(AVAILABLE)
                .build();
    }

    @Test
    @DisplayName("Map UserEntity to UserDto")
    void shouldMapUserEntityToUserDto() {
        UserDto userDto = userMapper.userToUserDto(user);

        assertEquals(user.getUuid(), userDto.uuid(),
                "UUID should be equal");
        assertEquals(user.getUsername(), userDto.username(),
                "Username should be equal");
        assertEquals(user.getEmail(), userDto.email(),
                "Email should be equal");
        assertEquals(user.getRole(), userDto.role(),
                "Role should be equal");
        assertTrue(userDto.available(),
                "User should be available");
    }

    @Test
    @DisplayName("Map UserDto to UserEntity")
    public void shouldMapUserDtoToUserEntity() {
        UserDto userDto = new UserDto(uuid, USERNAME, EMAIL, ROLE, AVAILABLE);
        UserEntity user = userMapper.userDtoToUser(userDto);

        assertEquals(userDto.uuid(), user.getUuid(),
                "UUID should be equal");
        assertEquals(userDto.username(), user.getUsername(),
                "Username should be equal");
        assertEquals(userDto.email(), user.getEmail(),
                "Email should be equal");
        assertEquals(userDto.role(), user.getRole(),
                "Role should be equal");
        assertTrue(userDto.available(),
                "User should be available");
    }

    @Test
    @DisplayName("Map list of UserEntity to list of UserDto")
    void shouldMapListOfUserEntityToListOfUserDto() {
        List<UserDto> userDtoList =
                userMapper.userEntityListToUserDtoList(java.util.List.of(user));

        assertEquals(1, userDtoList.size(),
                "List should contain only one element");
        assertEquals(user.getUuid(), userDtoList.getFirst().uuid(),
                "UUID should be equal");
        assertEquals(user.getUsername(), userDtoList.getFirst().username(),
                "Username should be equal");
        assertEquals(user.getEmail(), userDtoList.getFirst().email(),
                "Email should be equal");
        assertTrue(userDtoList.getFirst().available(),
                "User should be available");
    }

    @Test
    @DisplayName("Map RegisterUserRequest to UserEntity")
    void shouldMapURegisterUserRequestToUserEntity() {
        RegisterUserRequest userRequest = new RegisterUserRequest(USERNAME, EMAIL, PASSWORD);
        UserEntity registeredUser = userMapper.registerUserRequestToUser(userRequest);

        assertEquals(userRequest.username(), registeredUser.getUsername(),
                "Username should be equal");
        assertEquals(userRequest.email(), registeredUser.getEmail(),
                "Email should be equal");
        assertEquals(userRequest.password(), registeredUser.getPassword(),
                "Password should be equal");
    }

    @Test
    @DisplayName("Map UpdateUserRequest to UserEntity")
    void shouldMapUpdateUserRequestToUserEntity() {
        UpdateUserRequest updateUser = new UpdateUserRequest(uuid, USERNAME, EMAIL);
        UserEntity updatedUser = userMapper.updateUserRequestToUser(updateUser, user);

        assertEquals(updateUser.uuid(), updatedUser.getUuid(),
                "UUID should be equal");
        assertEquals(updateUser.username(), updatedUser.getUsername(),
                "Username should be equal");
        assertEquals(updateUser.email(), updatedUser.getEmail(),
                "Email should be equal");
    }

    @Test
    @DisplayName("Map page of UserEntity to page of UserDto")
    void shouldMapPageOfUserEntityToUserDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<UserEntity> listUsers = List.of(user);
        Page<UserEntity> pageOfUsers = new PageImpl<>(listUsers, pageable, listUsers.size());

        Page<UserDto> result = userMapper.pageUserEntityToPageUserDto(pageOfUsers);
        assertAll("Page entity to page dto assertions",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(1, result.getTotalElements(), "Should be one user"),
                () -> assertEquals(0, result.getNumber(), "Page number should be equal to zero"),
                () -> assertEquals(user.getUuid(), result.getContent().getFirst().uuid(),
                        "User UUID should be equal"));

    }
}
