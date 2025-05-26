package org.csc.chessclub.exception.validation;

import java.util.Map;

public record ValidErrorMessage(
        int statusCode,
        Map<String, String> errors
) {
}
