package com.bitbubble.api.app.contollers;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.data.Requests.VerifyCode;
import com.bitbubble.api.app.data.Requests.VerifyEmail;
import com.bitbubble.api.app.data.Response.Response;
import com.bitbubble.api.app.entitiy.Role;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.repository.UserRepo;
import com.bitbubble.api.app.service.RoleService;
import com.bitbubble.api.app.service.UserService;
import com.bitbubble.api.app.util.CommonUtil;
import com.bitbubble.api.app.util.events.SendEmailEvent;

import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommonUtil common;

    @PostMapping({ "/register_user" })
    private Map<String, Object> createUser(@RequestBody User user) {

        Response response = new Response();
        Map<String, Object> res = response.responseOk();
        Map<String, Object> err = response.responseError();
        try {
           
            Set<Role> userRole = roleService.findByRoleName("User");
            user.setRole(userRole);
            user.setUserPassword(CommonUtil.getEncodedPassword(user.getUserPassword(), passwordEncoder));
            user.setVerifyCode(common.RandomNumber());

            User userInfo = userService.createUser(user);
            if(userInfo.equals(null)){
                err.put("error", "error creating user please try again");
                return err;
            }
            
            res.put("data", userInfo);

            SendEmailEvent emv = new SendEmailEvent();
            emv.setEmailAddress(userInfo.getEmail());
            emv.setVerifyCode(userInfo.getVerifyCode());
            emv.setEmailSubject("LITBUBBLE EMAIL VERIFICATION");
            eventPublisher.publishEvent(emv);
            return res;

        } catch (Exception e) {
            err.put("data", e.getCause().getCause().getMessage());
            return err;
        }

    }

    @PostMapping("/token/verify")
    private Map<String, Object> verifyUserToken(@RequestBody VerifyCode token) {
       
        
        Response response = new Response();
        Map<String, Object> res = response.responseOk();
        Map<String, Object> err = response.responseError();
        try {
            
            if (!common.VerifyToken(token.getToken())) {
                res.put("data", "invalid token " + token.getToken());
                return res;
               
            }
           

            User user = userService.getUserByVerifyCode(token.getToken());
            
            if(user.equals(null)){
                err.put("data", "user not found");
                return err;
            }

            user.setIsVerified(true);
            userService.createUser(user);

            res.put("data", "verfied");
            res.put("user", user);

            return res;
        } catch (Exception e) {
           
            err.put("data", e.getMessage());
            return err;
        }

    }

    // cleect password and send mails
    @PostMapping("/forgot_password")
    private Map<String, Object> processForgotPassword(HttpServletRequest request, @RequestBody VerifyEmail email, HttpServletResponse resp) {
        CommonUtil commonUtil = new CommonUtil();
        Response response = new Response();
        String token = RandomString.make(30);
      

        try {
            userService.updateResetPasswordToken(token, email.getEmail());
            String resetPasswordLink = commonUtil.getSiteUrl(request) + "reset_password?token=" + token;

            SendEmailEvent emv = new SendEmailEvent();
            emv.setEmailAddress(email.getEmail());
            emv.setEmailSubject("RESET PASSWORD");
            emv.setResetToken(resetPasswordLink);
            eventPublisher.publishEvent(emv);

            Map<String, Object> res = response.responseOk();
            res.put("data", "verification code sent successfully");
            return res;
        } catch (Exception e) {
            Map<String, Object> err = response.responseError();
            err.put("data", e.getMessage());
            return err;
        }
    }

    // reset the password in data base
    // react should have the site url then param should be rest token
    // use resettoken to check if user is verified
    @PostMapping("/valid_reset_password")
    private Boolean processForgotPassword(@RequestBody String token) {
        try {
            User user = userService.getPasswordResetToken(token);
            if (user.getValidToReset().equals(true)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    @PostMapping("/update_passowrd")
    private Map<String, Object> updateUserPassword(@RequestBody String newPassword, @RequestBody String token) {
        Response response = new Response();
        try {
            User user = userService.getPasswordResetToken(token);
            userService.updatePassword(user, newPassword);

            Map<String, Object> res = response.responseOk();
            res.put("data", "verification code sent successfully");
            res.put("user", user);
            return res;
        } catch (Exception e) {
            Map<String, Object> err = response.responseError();
            err.put("data", e.getMessage());
            return err;
        }
    }

    @GetMapping("/auth/user")
    private Map<String, Object> getUser() {
        Response response = new Response();
        try {
            String userData = CommonUtil.getUserAuth();
            User user = userService.getUserByEmail(userData);
            if(user.equals(null)) {
                Map<String, Object> err = response.responseError();
                err.put("data", "invalid. user not found");
                return err;
            }
            Map<String, Object> res = response.responseOk();
            res.put("user", user);
            return res;
        } catch (Exception e) {
            Map<String, Object> err = response.responseError();
            err.put("data", e.getMessage());
            return err;
        }
    }

    @PostMapping("/auth/update")
    private Map<String, Object> updateUser(@RequestBody User userUpdate){
        Response response = new Response();
        try {
            String userData = CommonUtil.getUserAuth();
            User user = userService.getUserByEmail(userData);
            if(user.equals(null)) {
                Map<String, Object> err = response.responseError();
                err.put("data", "invalid. user not found");
                return err;
            }

            user.setCountry(userUpdate.getCountry());
            user.setPhoneNumber(userUpdate.getPhoneNumber());
            user.setPref(userUpdate.getPref());
            user.setState(userUpdate.getState());
            user.setPostal(userUpdate.getPostal());
            user.setWallet(userUpdate.getWallet());

            User savedUser = userRepo.save(user);

            Map<String, Object> res = response.responseOk();
            res.put("data", savedUser);
            return res;

        } catch (Exception e) {
           Map<String, Object> err = response.responseError();
           err.put("data", e.getMessage());
           return err;
        }
       
    }

}
