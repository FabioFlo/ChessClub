package org.csc.chessclub.exception.validation.result;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.csc.chessclub.enums.Result;

public class CustomResultValidator implements ConstraintValidator<ValidResult, Result> {

  @Override
  public boolean isValid(Result result, ConstraintValidatorContext constraintValidatorContext) {
    if (result == null) {
      return false;
    }
    return Arrays.asList(Result.values()).contains(result);
  }
}
