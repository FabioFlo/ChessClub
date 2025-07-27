package org.csc.chessclub.service.user;

import org.csc.chessclub.dto.user.RegisterUserRequest;
import org.csc.chessclub.dto.user.UpdateRoleDto;
import org.csc.chessclub.dto.user.UpdateUserRequest;
import org.csc.chessclub.dto.user.UserDto;

import java.util.UUID;
import org.csc.chessclub.enums.Role;

public interface UserService {

  UserDto create(RegisterUserRequest request);

  UserDto update(UpdateUserRequest userRequest);

  UserDto getById(UUID uuid);

  void delete(UUID uuid);

  Role updateUserRole(UpdateRoleDto updateRoleDto);

}
