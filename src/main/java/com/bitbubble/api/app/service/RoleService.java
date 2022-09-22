package com.bitbubble.api.app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.entitiy.Role;
import com.bitbubble.api.app.repository.RoleRepo;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role createNewRole(Role role) {
        return roleRepo.saveAndFlush(role);
    }

    public Set<Role> findByRoleName(String name){
        return roleRepo.findByRoleName(name);
    }
}
