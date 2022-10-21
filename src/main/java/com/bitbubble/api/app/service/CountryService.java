package com.bitbubble.api.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bitbubble.api.app.entitiy.Country;
import com.bitbubble.api.app.repository.CountryRepo;

@Service
public class CountryService {
    @Autowired
    CountryRepo countryRepo;

    public Country addCountries(Country country){
       return countryRepo.save(country);
    }

    public List<Country> getAllCountry() {
        return countryRepo.findAll();
    }
}
