package org.csc.chessclub.repository;

import java.util.List;
import java.util.UUID;
import org.csc.chessclub.model.tournament.TournamentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {

  List<TournamentEntity> getTournamentEntitiesByTitleAndAvailableTrue(String title);

  Page<TournamentEntity> findByAvailableIsTrue(Pageable pageable);

  @Modifying
  @Query("update TournamentEntity  t set t.available = false where t.uuid = :uuid")
  int setAvailableFalse(@Param("uuid") UUID uuid);

  Page<TournamentEntity> findTournamentEntitiesByEvent_UuidAndAvailableTrue(
      UUID eventUuid, Pageable pageable);
}
