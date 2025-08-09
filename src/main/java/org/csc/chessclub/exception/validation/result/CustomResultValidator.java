package org.csc.chessclub.exception.validation.result;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.csc.chessclub.enums.Result;

import java.util.Arrays;

public class CustomResultValidator implements ConstraintValidator<ValidResult, Result> {

    @Override
    public boolean isValid(Result result, ConstraintValidatorContext constraintValidatorContext) {
        if (result == null) {
            return false;
        }
        return Arrays.asList(Result.values()).contains(result);

    }
}
