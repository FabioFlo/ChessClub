package org.csc.chessclub.config.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.repository.EventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
@Log
public class EventMockData {
    public static final String AMAZING_EVENT_TITLE = "The amazing local chess event";
    public static final String WORLD_CUP_TITLE = "FIDE World Cup";

    private final EventRepository eventRepository;

    public void listOfEvents() {
        List<EventEntity> events = List.of(
                EventEntity.builder()
                        .title(AMAZING_EVENT_TITLE)
                        .available(true)
                        .author("The president")
                        .announcementPDF("second-edition.pdf")
                        .createdAt(LocalDate.parse("2024-02-10"))
                        .description("The best chess event of the city")
                        .build(),
                EventEntity.builder()
                        .title(WORLD_CUP_TITLE)
                        .available(true)
                        .author("FIDE")
                        .createdAt(LocalDate.parse("2000-09-01"))
                        .description("This knockout tournament determines three qualifiers for the Candidates Tournament.")
                        .build()
        );
        log.info("Saving events...");
        eventRepository.saveAll(events);
        log.info("Events saved!");
    }
}
