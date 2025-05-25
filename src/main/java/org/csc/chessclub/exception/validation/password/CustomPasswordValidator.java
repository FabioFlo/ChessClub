package org.csc.chessclub.exception.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class CustomPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        List<String> messages = new ArrayList<>();

        if (password.length() < 8) {
            messages.add(PasswordValidationMassage.PASSWORD_TOO_SHORT);
        }
        if (!password.matches(".*[A-Z].*")) {
            messages.add(PasswordValidationMassage.PASSWORD_MUST_CONTAIN_UPPERCASE_LETTER);
        }
        if (!password.matches(".*[a-z].*")) {
            messages.add("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            messages.add("Password must contain at least one digit");
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            messages.add("Password must contain at least one special character");
        }
        if (password.matches(".*\\s.*")) {
            messages.add("Password must not contain whitespace");
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
