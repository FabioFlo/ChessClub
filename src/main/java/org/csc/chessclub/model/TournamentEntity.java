package org.csc.chessclub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private boolean available;
    @OneToMany(mappedBy = "tournament")
    private Set<GameEntity> games;
    @ManyToOne
    @JoinColumn(name = "event_uuid")
    private EventEntity event;

//TODO: category attribute can be useful like CLUB, INTERNATIONAL, HISTORIC

}
