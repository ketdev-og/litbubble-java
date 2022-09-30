package com.bitbubble.api.app.data.Response;

import com.bitbubble.api.app.entitiy.RefreshToken;
import com.bitbubble.api.app.entitiy.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private User user;
    private String jwtToken;   
    private RefreshToken refreshToken;  
}
