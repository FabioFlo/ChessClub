package org.csc.chessclub.exception;

import java.io.Serial;

public class CustomNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomNotFoundException(String message) {
        super(message);
    }

}
