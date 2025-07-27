package org.csc.chessclub;

import org.csc.chessclub.exception.validation.file.FileValidationProperties;
import org.csc.chessclub.service.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, FileValidationProperties.class})
public class ChessClubApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChessClubApplication.class, args);
  }
  //TODO: add cache

  //TODO: create docker compose
  //TODO: write documentation .md file, flowchart, commands for test, test percentage etc

}
