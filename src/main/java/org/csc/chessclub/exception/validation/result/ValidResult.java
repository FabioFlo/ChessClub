package org.csc.chessclub.exception.validation.result;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = CustomResultValidator.class)
public @interface ValidResult {

  String message() default ResultValidationMessage.RESULT_MUST_BE_VALID;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
