package com.bitbubble.api.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User getUserByVerifyCode(String token) {
        return userRepo.findByVerifyCode(token);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User getPasswordResetToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updateResetPasswordToken(String token, String email) {
        User user = userRepo.findByEmail(email);
        user.setResetPasswordToken(token);
        user.setValidToReset(true);
        userRepo.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepo.save(user);
    }
}
