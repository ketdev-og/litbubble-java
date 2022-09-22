package com.bitbubble.api.app.util;


import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class jwtutil {

    private static final String SECRET_KEY = "this_is_ketems_code";
    

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
        
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
            final Claims claims = getAllClaimsFromToken(token);
            return claimResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
       String userName = getUserNameFromToken(token);
       return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
            final Date expirationDate = getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
    }

    public Date getExpirationDateFromToken(String token){
            return getClaimFromToken(token, Claims::getExpiration);
    }

}
