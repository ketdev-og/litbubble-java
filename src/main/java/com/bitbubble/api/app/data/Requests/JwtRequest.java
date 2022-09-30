package com.bitbubble.api.app.data.Requests;

import lombok.Data;

@Data
public class JwtRequest {
    private String userName;
    private String userPassword;

}
