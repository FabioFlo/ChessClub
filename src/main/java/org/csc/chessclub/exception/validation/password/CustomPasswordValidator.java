package org.csc.chessclub.exception.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        List<String> messages = new ArrayList<>();

        if (password.length() < 8) {
            messages.add(PasswordValidationMassage.PASSWORD_TOO_SHORT);
        }
        if (!password.matches(".*[A-Z].*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_UPPERCASE_LETTER);
        }
        if (!password.matches(".*[a-z].*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_LOWERCASE_LETTER);
        }
        if (!password.matches(".*\\d.*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_DIGIT);
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER);
        }
        if (password.matches(".*\\s.*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_NOT_CONTAIN_WHITESPACE);
        }

        if (!messages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            messages.forEach(message ->
                    context
                            .buildConstraintViolationWithTemplate(message)
                            .addConstraintViolation());
            return false;
        }
        return true;
    }
}
