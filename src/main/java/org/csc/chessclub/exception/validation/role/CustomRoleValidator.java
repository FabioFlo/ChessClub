package org.csc.chessclub.exception.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.csc.chessclub.enums.Role;

public class CustomRoleValidator implements ConstraintValidator<ValidRole, Role> {

  @Override
  public boolean isValid(Role role, ConstraintValidatorContext constraintValidatorContext) {
    if (role == null) {
      return false;
    }

    return Arrays.asList(Role.values()).contains(role);
  }
}
