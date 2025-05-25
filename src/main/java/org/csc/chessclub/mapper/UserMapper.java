package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity user);

    UserEntity userDtoToUser(UserDto userDto);

    List<UserDto> userEntityListToUserDtoList(List<UserEntity> userEntityList);

    UserEntity registerUserRequestToUser(RegisterUserRequest userRequest);

    UserEntity updateUserRequestToUser(UserEntity user);
}
