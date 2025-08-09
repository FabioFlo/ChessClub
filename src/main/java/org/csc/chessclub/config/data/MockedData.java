package org.csc.chessclub.config.data;

import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;

import java.time.LocalDate;
import java.util.List;


public abstract class MockedData {


    public List<EventEntity> listOfEvents() {
        return List.of(
                EventEntity.builder()
                        .title("The amazing local chess event")
                        .available(true)
                        .author("The president")
                        .announcementPDF("first.pdf")
                        .createdAt(LocalDate.parse("2025-02-10"))
                        .description("The best chess event of the city")
                        .build(),
                EventEntity.builder()
                        .title("FIDE World Cup")
                        .available(true)
                        .author("FIDE")
                        .createdAt(LocalDate.parse("2000-09-01"))
                        .description("This knockout tournament determines three qualifiers for the Candidates Tournament.")
                        .build()
        );

    }

    public List<TournamentEntity> listOfTournaments() {
        return List.of(

        );
    }
}
