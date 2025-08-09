package org.csc.chessclub.model.user;

public final class UserConstraints {

    private UserConstraints() {
    }

    public static final int USERNAME_MAX_LENGTH = 30;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int EMAIL_MIN_LENGTH = 6;
    public static final int EMAIL_MAX_LENGTH = 255;
}
