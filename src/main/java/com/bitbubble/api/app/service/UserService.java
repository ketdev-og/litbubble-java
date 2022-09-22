package com.bitbubble.api.app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.repository.UserRepo;

import lombok.NoArgsConstructor;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(User user){
        return userRepo.save(user);
    }

   
}
