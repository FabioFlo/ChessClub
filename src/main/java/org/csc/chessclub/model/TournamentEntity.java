package org.csc.chessclub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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


}
