package com.bitbubble.api.app.service;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.data.Requests.RefreshRequest;
import com.bitbubble.api.app.entitiy.RefreshToken;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.repository.RefreshRepo;
import com.bitbubble.api.app.repository.UserRepo;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshRepo refreshRepo;

    @Autowired
    private UserRepo userRepo;

    public Optional<RefreshToken> findByToken(RefreshRequest token) {
        return refreshRepo.findByToken(token.getRefreshTokenString());
    }

    public RefreshToken createRefreshToken(Long userId) {
        deleteByUserId(userId);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepo.findById(userId).get());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken = refreshRepo.save(refreshToken);
        return refreshToken;

    }

    public RefreshToken verifyExpiration(RefreshToken token) throws Exception {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshRepo.delete(token);
            throw new Exception(token.getToken().toString() + "   token already expired");
        }

        return token;
    }


  
    public long deleteByUserId(Long userId) {
        return refreshRepo.deleteByUser(userRepo.findById(userId).get());
    }
}
