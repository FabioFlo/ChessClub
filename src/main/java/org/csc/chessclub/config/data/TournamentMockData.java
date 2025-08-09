package org.csc.chessclub.config.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.csc.chessclub.repository.EventRepository;
import org.csc.chessclub.repository.TournamentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.csc.chessclub.config.data.EventMockData.AMAZING_EVENT_TITLE;
import static org.csc.chessclub.config.data.EventMockData.WORLD_CUP_TITLE;

@Component
@Profile("local")
@RequiredArgsConstructor
@Log
public final class TournamentMockData {
    public static final String AMAZING_TOURNAMENT_TITLE_FIRST = "The amazing local chess tournament - first edition";
    public static final String AMAZING_TOURNAMENT_TITLE_SECOND = "The amazing local chess tournament - second edition";
    public static final String WORLD_CUP_2023 = "Chess World Cup 2023";
    private final TournamentRepository tournamentRepository;
    private final EventRepository eventRepository;

    public void listOfTournaments() {
        List<EventEntity> events = eventRepository.findAll();
        EventEntity amazingEvent = findEventByTitle(AMAZING_EVENT_TITLE, events);
        EventEntity worldCup = findEventByTitle(WORLD_CUP_TITLE, events);

        List<TournamentEntity> tournamentEntities = List.of(
                TournamentEntity.builder()
                        .title(AMAZING_TOURNAMENT_TITLE_FIRST)
                        .description("The first edition of the best chess event of the city!")
                        .startDate(LocalDate.parse("2024-03-01"))
                        .endDate(LocalDate.parse("2024-03-03"))
                        .event(amazingEvent)
                        .available(true)
                        .build(),
                TournamentEntity.builder()
                        .title(AMAZING_TOURNAMENT_TITLE_SECOND)
                        .description("The second edition of the best chess event of the city! At the same place and time, are you ready to test your skills?")
                        .startDate(LocalDate.parse("2025-03-01"))
                        .endDate(LocalDate.parse("2025-03-03"))
                        .event(amazingEvent)
                        .available(true)
                        .build(),
                TournamentEntity.builder()
                        .title(WORLD_CUP_2023)
                        .description("The tournament was an eight-round knockout event, with the top 50 seeds having been given a bye directly into the second round. The losers of the two semi-finals played a match for third place. The players who finished first, second, and third qualified for the Candidates Tournament 2024, a tournament to decide the challenger for the upcoming World Championship.")
                        .startDate(LocalDate.parse("2023-07-30"))
                        .endDate(LocalDate.parse("2023-08-24"))
                        .event(worldCup)
                        .available(true)
                        .build()
        );
        log.info("Saving tournaments");
        tournamentRepository.saveAll(tournamentEntities);
    }


    private EventEntity findEventByTitle(String title, List<EventEntity> events) {
        return events.stream()
                .filter(e -> e.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }


}
