package com.bitbubble.api.app.service;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.entitiy.JwtRequest;
import com.bitbubble.api.app.entitiy.JwtResponse;
import com.bitbubble.api.app.repository.UserRepo;
import com.bitbubble.api.app.util.jwtutil;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private jwtutil jwtutil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager; 

    public JwtResponse createJwtResponse(JwtRequest jwtRequest){
       String userName = jwtRequest.getUserName();
       String userPassword = jwtRequest.getUserPassword();
       
       
       return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private void authenticate(String userName, String userPassword) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
                throw new Exception("user disabled");
        } catch (BadCredentialsException e){
            throw new Exception("bad credentials from user");
        }
        
    }
    
}
