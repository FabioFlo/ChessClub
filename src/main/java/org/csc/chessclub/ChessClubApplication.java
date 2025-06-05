package org.csc.chessclub;

import org.csc.chessclub.service.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ChessClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessClubApplication.class, args);
    }

}
