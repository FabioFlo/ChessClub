package org.csc.chessclub.exception.validation.file;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface ValidFile {

  String message() default FileValidationMessage.FILE_NOT_VALID;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
