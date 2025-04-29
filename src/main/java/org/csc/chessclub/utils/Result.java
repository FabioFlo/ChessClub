package org.csc.chessclub.utils;

import lombok.Getter;

@Getter
public enum Result {
    WhiteWon("1-0"),
    BlackWon("0-1"),
    Draw("1/2 - 1/2");

    private final String value;

    Result(String value) {
        this.value = value;
    }

}
