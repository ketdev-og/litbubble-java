package com.bitbubble.api.app.util.Interceptors;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.service.UserService;


@Component
public class VerifyCodeInterceptor implements HandlerInterceptor{

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
       UserDetails loggedIn = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =  userService.getUserByEmail(loggedIn.getUsername());
       
        
        if(user.getIsVerified().equals(false)){
            response.sendError(400, "user not verified");
            return false;
        }
       return true;
    }
}
