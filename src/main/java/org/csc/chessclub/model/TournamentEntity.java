package org.csc.chessclub.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tournaments")
public class TournamentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    private String firstPlace;
    private String secondPlace;
    private String thirdPlace;
    @OneToOne()
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private EventEntity event;
    @OneToMany()
    private List<GamesEntity> games;

}
