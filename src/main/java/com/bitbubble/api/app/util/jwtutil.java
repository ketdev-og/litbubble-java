package com.bitbubble.api.app.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bitbubble.api.app.entitiy.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class jwtutil {

    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final int TOKEN_VALIDITY = 3600 * 5;

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);

    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        Map <String, Object> claims = new HashMap<>();
       
        return Jwts.builder().setClaims(claims)
                                .setSubject(userDetails.getUsername())
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY * 1000))
                                .signWith(key)
                                .compact();

    }

    public String generateTokenByName(User user){
        Map <String, Object> claims = new HashMap<>();
       
        return Jwts.builder().setClaims(claims)
                                .setSubject(user.getEmail())
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY * 1000))
                                .signWith(key)
                                .compact();
    }

}
