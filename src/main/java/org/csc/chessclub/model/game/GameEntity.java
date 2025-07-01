package org.csc.chessclub.model.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csc.chessclub.enums.Result;
import org.csc.chessclub.model.tournament.TournamentEntity;

import java.util.UUID;

@Entity
@Data
@Table(name = "games")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue
    private UUID uuid;
    @Column(length = GameConstraints.PLAYER_MAX_LENGTH)
    private String whitePlayerName;
    @Column(length = GameConstraints.PLAYER_MAX_LENGTH)
    private String blackPlayerName;
    private Result result;
    private String pgn;
    private boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_uuid")
    private TournamentEntity tournament;
}
