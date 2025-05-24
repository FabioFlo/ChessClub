package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.UserDto;
import org.csc.chessclub.enums.Role;
import org.csc.chessclub.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {
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
    @DisplayName("Map User entity to user dto")
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
    }

    @Test
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
    }
}
