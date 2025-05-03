package org.csc.chessclub.repository;

import org.csc.chessclub.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {
    List<EventEntity> findEventEntityByTitle(String title);
    List<EventEntity> findEventEntityByAuthor(String author);
}
