package org.csc.chessclub.enums;

import lombok.Getter;

@Getter
public enum NotFoundMessage {
     EVENT("Event not found"),
     EVENT_WITH_UUID("Event with UUID %s not found"),
     USER("User not found"),
    USER_WITH_UUID("User with UUID %s not found");

     private final String message;

     NotFoundMessage(String message) {
        this.message = message;
     }

     public String format(Object... args) {
        return message.formatted(args);
     }
}
