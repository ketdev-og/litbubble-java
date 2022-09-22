package com.bitbubble.api.app.repository;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitbubble.api.app.entitiy.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
   public Set<Role> findByRoleName(String roleName);
}
