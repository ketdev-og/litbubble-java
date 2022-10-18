package com.bitbubble.api.app.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bitbubble.api.app.service.JwtService;
import com.bitbubble.api.app.util.jwtutil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;



@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private jwtutil jwtutil;

    @Autowired
    private JwtService jService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
       
        String jwtToken = null;
        String userName = null;
    
        if (header != null && header.startsWith("Bearer ")) {
            jwtToken = header.substring("Bearer ".length());
            System.err.println(jwtToken);
            
            try {
                userName = jwtutil.getUserNameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.err.println(e + "Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                System.err.println("Jwt token is expired");
            }
        } else {
            System.err.println("jwt token doesnt start with bearer");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jService.loadUserByUsername(userName);
            if (jwtutil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, userDetails.getAuthorities());

                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
   
        filterChain.doFilter(request, response);
        
    }

}
