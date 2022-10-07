package com.bitbubble.api.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitbubble.api.app.entitiy.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByVerifyCode(String token);

    User findByResetPasswordToken(String token);
    
}
