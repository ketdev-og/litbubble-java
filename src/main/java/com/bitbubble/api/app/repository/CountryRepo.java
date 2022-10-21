package com.bitbubble.api.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitbubble.api.app.entitiy.Country;

@Repository
public interface CountryRepo extends JpaRepository<Country, String> {
    
}
