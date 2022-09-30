package com.bitbubble.api.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bitbubble.api.app.data.Requests.JwtRequest;
import com.bitbubble.api.app.data.Response.JwtResponse;
import com.bitbubble.api.app.entitiy.RefreshToken;
import com.bitbubble.api.app.entitiy.Role;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.repository.UserRepo;
import com.bitbubble.api.app.util.jwtutil;

import antlr.collections.List;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private jwtutil jwtutil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    public JwtResponse createJwtResponse(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName, userPassword);
        UserDetails userDetails = loadUserByUsername(userName);
        String newgeneratedToken = jwtutil.generateToken(userDetails);
        User user = userRepo.findByEmail(userName).get();
        RefreshToken token = refreshTokenService.createRefreshToken(user.getId());

        return new JwtResponse(user, newgeneratedToken, token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).get();
        System.out.println(getAuthorities(user));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getUserPassword(),
                getAuthorities(user));

    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("user is disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("bad credentials from user");
        }

    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()));
        });

        return authorities;
    }

}
