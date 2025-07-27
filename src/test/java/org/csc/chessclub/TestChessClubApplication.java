package org.csc.chessclub;

import org.springframework.boot.SpringApplication;

public class TestChessClubApplication {

  public static void main(String[] args) {
    SpringApplication.from(ChessClubApplication::main).with(TestcontainersConfiguration.class)
        .run(args);
  }

}
