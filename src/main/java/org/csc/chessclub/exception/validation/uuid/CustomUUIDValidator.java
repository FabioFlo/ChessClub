package org.csc.chessclub.exception.validation.uuid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class CustomUUIDValidator implements ConstraintValidator<ValidUUID, UUID> {

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return value != null && !value.equals(new UUID(0, 0));
    }
}


