package org.csc.chessclub.repository;

import org.csc.chessclub.model.tournament.TournamentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {

    List<TournamentEntity> getTournamentEntitiesByTitleAndAvailableTrue(String title);
    Page<TournamentEntity> getDistinctByAvailableIsTrue(Pageable pageable);
}
