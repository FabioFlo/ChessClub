package org.csc.chessclub.repository;

import org.csc.chessclub.model.event.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

  List<EventEntity> findEventEntityByTitleAndAvailableTrue(String title);

  List<EventEntity> findEventEntityByAuthorAndAvailableTrue(String author);

  Page<EventEntity> findAllByAvailableTrue(Pageable pageable);

  @Modifying
  @Query("update EventEntity e set e.available = false where e.uuid = :uuid")
  int setAvailableFalse(@Param("uuid") UUID uuid);
}
