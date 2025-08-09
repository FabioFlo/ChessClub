package org.csc.chessclub.exception.validation.messages;

import lombok.Getter;
import org.csc.chessclub.model.event.EventConstraints;

@Getter
public final class EventValidationMessage {

    private EventValidationMessage() {
    }

    public static final String TITLE_MUST_NOT_BE_BLANK = "Title must not be blank";
    public static final String DESCRIPTION_MUST_NOT_BE_BLANK = "Description must not be blank";
    public static final String AUTHOR_MUST_NOT_BE_BLANK = "Author must not be blank";
    public static final String TITLE_LENGTH_REQUIRED = "Title must be between "
            + EventConstraints.TITLE_MIN_LENGTH
            + " and "
            + EventConstraints.TITLE_MAX_LENGTH + " characters";
    public static final String AUTHOR_LENGTH_REQUIRED = "Author must be between "
            + EventConstraints.AUTHOR_MIN_LENGTH
            + " and "
            + EventConstraints.AUTHOR_MAX_LENGTH + " characters";
    public static final String DESCRIPTION_LENGTH_REQUIRED = "Description must be between "
            + EventConstraints.DESCRIPTION_MIN_LENGTH
            + " and "
            + EventConstraints.DESCRIPTION_MAX_LENGTH + " characters";
}
