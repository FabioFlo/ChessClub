package org.csc.chessclub.repository;

import org.csc.chessclub.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByUsernameOrEmail(String username, String email);

    Optional<UserEntity> findByUsernameOrEmailAndUuidNot(String username, String email, UUID uuid);

    @Modifying
    @Query("update UserEntity u set u.available = false where u.uuid = :uuid")
    int setAvailableFalse(@Param("uuid") UUID uuid);
}
