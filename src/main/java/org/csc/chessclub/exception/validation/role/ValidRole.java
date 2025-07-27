package org.csc.chessclub.exception.validation.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.csc.chessclub.exception.validation.result.ResultValidationMessage;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = CustomRoleValidator.class)
public @interface ValidRole {

  String message() default ResultValidationMessage.RESULT_MUST_BE_VALID;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
