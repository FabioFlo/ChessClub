package org.csc.chessclub.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.csc.chessclub.utils.ValidUUID;

import java.util.UUID;

public class CustomUUIDValidator implements ConstraintValidator<ValidUUID, UUID> {
    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return value != null && !value.equals(new UUID(0, 0));
    }
}


