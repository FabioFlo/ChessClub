package org.csc.chessclub.exception.validation.messages;

import org.csc.chessclub.model.user.UserConstraints;

public final class UserValidationMessage {
    private UserValidationMessage() {
    }

    public static final String USERNAME_MUST_NOT_BE_BLANK = "Username must not be blank";
    public static final String USERNAME_LENGTH_REQUIRED = "Username should be between "
            + UserConstraints.USERNAME_MIN_LENGTH
            + " and "
            + UserConstraints.USERNAME_MAX_LENGTH + " characters";
    public static final String EMAIL_MUST_BE_VALID = "Email must be valid";
    public static final String EMAIL_LENGTH_REQUIRED = "Email should be between "
            + UserConstraints.EMAIL_MIN_LENGTH
            + " and "
            + UserConstraints.EMAIL_MAX_LENGTH + " characters";

}
