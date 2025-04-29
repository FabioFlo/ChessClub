package org.csc.chessclub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.csc.chessclub.utils.Result;

import java.util.UUID;

@Entity
@Data
@Table(name = "games")
public class GamesEntity {
    @Id
    @GeneratedValue
    private UUID uuid;
    private String white;
    private String black;
    private Result result;
    private String pgn;
}
