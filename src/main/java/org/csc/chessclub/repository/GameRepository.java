package org.csc.chessclub.repository;

import org.csc.chessclub.model.game.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    Page<GameEntity> findByAvailableTrueAndWhitePlayerNameOrBlackPlayerNameIs(String whitePlayer, String blackPlayer,
                                                                              Pageable pageable);

    Page<GameEntity> findByAvailableTrueAndWhitePlayerNameIs(String whitePlayer,
                                                             Pageable pageable);

    Page<GameEntity> findByAvailableTrueAndBlackPlayerNameIs(String blackPlayer,
                                                             Pageable pageable);

    Page<GameEntity> findAllByAvailableTrue(Pageable pageable);
}
