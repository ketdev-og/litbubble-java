package com.bitbubble.api.app.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.entitiy.JwtRequest;
import com.bitbubble.api.app.service.JwtService;

@RestController
@CrossOrigin
public class JwtController {
        @Autowired
        private JwtService jwtService;



        @PostMapping({"/authenticate"})
        public void createJwtToken(@RequestBody JwtRequest jwtRequest){

        }
}
