package org.csc.chessclub.service.user;

import org.csc.chessclub.dto.user.*;

import java.util.UUID;
import org.csc.chessclub.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserDto create(RegisterUserRequest request);

  UserDto update(UpdateUserRequest userRequest);

  UserDto getById(UUID uuid);

  void delete(UUID uuid);

  Role updateUserRole(UpdateRoleDto updateRoleDto);

  Page<UserDto> getAll(Pageable pageable);

  void updateUserPassword(UpdatePasswordDto updatePasswordDto);

}
