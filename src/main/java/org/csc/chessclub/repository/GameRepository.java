package org.csc.chessclub.repository;

import org.csc.chessclub.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    List<GameEntity> findGameEntitiesByWhitePlayerOrBlackPlayer(String whitePlayer, String blackPlayer);
}
