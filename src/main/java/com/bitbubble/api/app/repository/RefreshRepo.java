package com.bitbubble.api.app.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.bitbubble.api.app.entitiy.RefreshToken;
import com.bitbubble.api.app.entitiy.User;


@Repository
public interface RefreshRepo extends JpaRepository <RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

   Optional <User>findByUser(User user);

   @Transactional
    long deleteByUser(User user);
}
