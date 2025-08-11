package org.csc.chessclub.auth;

public record AuthenticationRequest(String usernameOrEmail, String password) {}
