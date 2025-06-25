package org.csc.chessclub.repository;

import org.csc.chessclub.model.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {

    List<TournamentEntity> getTournamentEntitiesByTitleAndAvailableTrue(String title);
    List<TournamentEntity> getDistinctByAvailableIsTrue();
}
