package org.csc.chessclub.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csc.chessclub.model.tournament.TournamentEntity;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    @Column(length = EventConstraints.MAX_TITLE_LENGTH)
    private String title;
    @Column(length = EventConstraints.MAX_DESCRIPTION_LENGTH)
    private String description;
    private LocalDate createdAt;
    @Column(length = EventConstraints.MAX_AUTHOR_LENGTH)
    private String author;
    private String announcementPDF;
    private boolean available;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<TournamentEntity> tournaments;

}
