package org.csc.chessclub.mapper;

import org.csc.chessclub.dto.UserDto;
import org.csc.chessclub.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity user);
}
