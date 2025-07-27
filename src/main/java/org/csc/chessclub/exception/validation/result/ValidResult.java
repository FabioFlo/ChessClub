package org.csc.chessclub.exception.validation.result;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import org.csc.chessclub.exception.validation.role.RoleValidationMessage;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = CustomResultValidator.class)
public @interface ValidResult {

  String message() default RoleValidationMessage.ROLE_MUST_BE_VALID;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
