package org.csc.chessclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TestChessClubApplication {

    public static void main(String[] args) {
        SpringApplication.from(ChessClubApplication::main).with(TestcontainersConfiguration.class)
                .run(args);
    }

}
