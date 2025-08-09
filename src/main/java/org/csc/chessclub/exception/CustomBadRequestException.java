package org.csc.chessclub.exception;

import java.io.Serial;

public class CustomBadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomBadRequestException(String message) {
        super(message);
    }
}
