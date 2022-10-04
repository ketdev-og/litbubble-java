package com.bitbubble.api.app.contollers;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.data.Response.Response;
import com.bitbubble.api.app.entitiy.Role;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.service.RoleService;
import com.bitbubble.api.app.service.UserService;
import com.bitbubble.api.app.util.CommonUtil;
import com.bitbubble.api.app.util.events.SendEmailEvent;

@RestController
public class UserController {

  
    @Autowired
    ApplicationEventPublisher eventPublisher;
   
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping({ "/register_user" })
    private Map<String, Object> createUser(@RequestBody User user) {
        CommonUtil commonUtil = new CommonUtil();
        try {
            Set<Role> userRole = roleService.findByRoleName("User");
            user.setRole(userRole);
            user.setUserPassword(getEncodedPassword(user.getUserPassword()));

            user.setVerifyCode(commonUtil.RandomNumber());

            Response response = new Response();
            User userInfo = userService.createUser(user);
            Map<String, Object> res = response.responseOk();
            res.put("data", userInfo);

            SendEmailEvent emv = new SendEmailEvent();
            emv.setEmailAddress(userInfo.getEmail());
            emv.setVerifyCode(userInfo.getVerifyCode());
            emv.setEmailSubject("LITBUBBLE EMAIL VERIFICATION");
            
            eventPublisher.publishEvent(emv);
            return res;
        } catch (Exception e) {
            Response response = new Response();
            Map<String, Object> err = response.responseError();
            err.put("data", e.getMessage());
            return err;
        }

    }

    private String getUserAuth() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping({ "/auth/admin/admin_url" })
    public String forAdmin() {
        try {
            return "Admin url";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping({ "/auth/user/user_url" })
    public String forUser() {
        try {
            
            return "user url";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
