package org.csc.chessclub.exception.validation.messages;

import java.util.Map;

public record ValidErrorMessage(
        int statusCode,
        Map<String, String> errors
) {
}
