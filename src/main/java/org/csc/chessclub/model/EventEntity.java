package org.csc.chessclub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String title;
    private String description;
    private LocalDate createdAt;
    private String author;
    private String announcementPDF;
    private boolean available;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Set<TournamentEntity>  tournaments;

}
