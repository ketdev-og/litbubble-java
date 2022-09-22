package com.bitbubble.api.app.entitiy;

import lombok.Data;

@Data
public class JwtRequest {
    private String userName;
    private String userPassword;

}
