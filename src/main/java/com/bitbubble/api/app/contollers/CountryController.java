package com.bitbubble.api.app.contollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.entitiy.Country;
import com.bitbubble.api.app.service.CountryService;

@RestController
@CrossOrigin
public class CountryController {

    @Autowired
    CountryService countryService;


    @PostMapping("/country")
    public Country addCountries(@RequestBody Country country) {
        return countryService.addCountries(country);
            
    }

    @GetMapping("/country")
    public List<Country> getCountries(){
        return countryService.getAllCountry();
    }
}
