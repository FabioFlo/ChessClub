package org.csc.chessclub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    private String title;
    private String description;
    private LocalDate date;
    private String author;
    private String announcementPDF;
    @OneToOne()
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private TournamentEntity tournament;
}
