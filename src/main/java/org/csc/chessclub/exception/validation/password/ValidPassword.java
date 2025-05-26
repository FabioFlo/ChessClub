package org.csc.chessclub.exception.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomPasswordValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default PasswordValidationMassage.PASSWORD_NOT_VALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
