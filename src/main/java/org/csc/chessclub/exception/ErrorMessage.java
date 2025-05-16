package org.csc.chessclub.exception;

public record ErrorMessage(
        String message,
        int statusCode,
        String timestamp) {
}
