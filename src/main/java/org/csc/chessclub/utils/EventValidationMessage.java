package org.csc.chessclub.utils;

import lombok.Getter;

@Getter
public class EventValidationMessage {
    public static final String UUID_MUST_BE_VALID = "UUID must be valid";
    public static final String TITLE_MUST_NOT_BE_BLANK = "Title must not be blank";
    public static final String DESCRIPTION_MUST_NOT_BE_BLANK = "Description must not be blank";
    public static final String AUTHOR_MUST_NOT_BE_BLANK = "Author must not be blank";
    public static final String TITLE_MUST_BE_BETWEEN_2_AND_100_CHARACTERS = "Title must be between 2 and 100 characters";
    public static final String AUTHOR_MUST_BE_BETWEEN_2_AND_30_CHARACTERS = "Author must be between 2 and 30 characters";


}
