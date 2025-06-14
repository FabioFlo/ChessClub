package org.csc.chessclub.repository;

import org.csc.chessclub.model.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    Page<GameEntity> findGameEntitiesByWhitePlayerNameOrBlackPlayerName(String whitePlayer, String blackPlayer,
                                                                        Pageable pageable);

    List<GameEntity> findGameEntitiesByWhitePlayerName(String whitePlayer);

    List<GameEntity> findGameEntitiesByBlackPlayerName(String blackPlayer);
}
