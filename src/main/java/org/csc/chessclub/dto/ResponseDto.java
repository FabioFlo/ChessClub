package org.csc.chessclub.dto;

public record ResponseDto<T>(T data, String message, boolean success) {}
