package com.bitbubble.api.app.util;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.service.UserService;

public class CommonUtil {


    @Autowired
    private UserService userService;

    
    public String RandomNumber(){
        Random rad =  new Random();
        int number = rad.nextInt(999999);
        return String.format("%06d", number);
    }

    public static String getEncodedPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }


    private String getUserAuth() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public Boolean VerifyToken(String token) {
        String user = getUserAuth();

        User userToken = userService.getVerifyCode(token);

        if (user.equals(userToken.getEmail())) {
            return true;
        }
        return false;

    }

    public String getSiteUrl(HttpServletRequest request){
        String siteUrl =  request.getRequestURI().toString();

        //localhost:8080/verifytoken should be replaced by //localhost:8080
        return siteUrl.replace(request.getServletPath(), "");
    }
}
