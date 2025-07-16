package org.csc.chessclub.model.tournament;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csc.chessclub.model.event.EventEntity;
import org.csc.chessclub.model.game.GameEntity;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "tournaments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    @Column(length = TournamentConstraints.TITLE_MAX_LENGTH)
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(length = TournamentConstraints.DESCRIPTION_MAX_LENGTH)
    private String description;
    private boolean available;
    @OneToMany(mappedBy = "tournament")
    private Set<GameEntity> games;
    @ManyToOne
    @JoinColumn(name = "event_uuid")
    private EventEntity event;
}
