package com.bitbubble.api.app.entitiy;

import lombok.Data;

@Data
public class JwtResponse {
    private User user;
    private String jwtToken;     
}
