package org.csc.chessclub.model.event;

public final class EventConstraints {
    private EventConstraints() {
    }
    public static final int TITLE_MIN_LENGTH = 5;
    public static final int TITLE_MAX_LENGTH = 50;
    public static final int DESCRIPTION_MIN_LENGTH = 5;
    public static final int DESCRIPTION_MAX_LENGTH = 500;
    public static final int AUTHOR_MAX_LENGTH = 30;
    public static final int AUTHOR_MIN_LENGTH = 2;
}
