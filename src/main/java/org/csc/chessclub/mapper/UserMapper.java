package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;
import org.csc.chessclub.model.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity user);

    @Mapping(target = "password", ignore = true)
    UserEntity userDtoToUser(UserDto userDto);

    List<UserDto> userEntityListToUserDtoList(List<UserEntity> userEntityList);

    default Page<UserDto> pageUserEntityToPageUserDto(Page<UserEntity> users) {
        return users.map(this::userToUserDto);
    }

    @Mapping(target = "available", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserEntity registerUserRequestToUser(RegisterUserRequest userRequest);

    UserEntity updateUserRequestToUser(UpdateUserRequest updateUserRequest,
                                       @MappingTarget UserEntity user);
}
