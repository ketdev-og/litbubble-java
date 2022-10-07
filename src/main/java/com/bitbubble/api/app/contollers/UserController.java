package com.bitbubble.api.app.contollers;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.aspectj.apache.bcel.classfile.Utility;
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
public class UserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping({ "/register_user" })
    private Map<String, Object> createUser(@RequestBody User user) {

        CommonUtil commonUtil = new CommonUtil();
        try {

            Set<Role> userRole = roleService.findByRoleName("User");
            System.out.println(userRole);
            user.setRole(userRole);

            user.setUserPassword(CommonUtil.getEncodedPassword(user.getUserPassword(), passwordEncoder));
            user.setVerifyCode(commonUtil.RandomNumber());

            User userInfo = userService.createUser(user);

            Map<String, Object> res = Response.responseOk();
            res.put("data", userInfo);

            SendEmailEvent emv = new SendEmailEvent();
            emv.setEmailAddress(userInfo.getEmail());
            emv.setVerifyCode(userInfo.getVerifyCode());
            emv.setEmailSubject("LITBUBBLE EMAIL VERIFICATION");

            eventPublisher.publishEvent(emv);
            return res;
        } catch (Exception e) {

            Map<String, Object> err = Response.responseError();
            err.put("data", e.getMessage());
            return err;
        }

    }

    @PostMapping("/token/verify")
    private Map<String, Object> verifyUserToken(@RequestBody VerifyCode token) {
        CommonUtil common = new CommonUtil();
        try {

            Map<String, Object> res = Response.responseOk();

            if (!common.VerifyToken(token.getToken())) {
                res.put("data", "invalid token " + token.getToken());
                return res;
            }

            User user = userService.getVerifyCode(token.getToken());
            user.setIsVerified(true);
            userService.createUser(user);

            res.put("data", "verfied");
            res.put("user", user);

            return res;
        } catch (Exception e) {
            Map<String, Object> err = Response.responseError();
            err.put("data", e.getMessage());
            return err;
        }

    }

    // cleect password and send mails
    @PostMapping("/forgot_password")
    private Map<String, Object> processForgotPassword(HttpServletRequest request, @RequestBody VerifyEmail email) {
        CommonUtil commonUtil = new CommonUtil();
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email.getEmail());
            String resetPasswordLink = commonUtil.getSiteUrl(request) + "reset_password?token=" + token;

            SendEmailEvent emv = new SendEmailEvent();
            emv.setEmailAddress(email.getEmail());
            emv.setEmailSubject("RESET PASSWORD");
            emv.setResetToken(resetPasswordLink);
            eventPublisher.publishEvent(emv);

            Map<String, Object> res = Response.responseOk();
            res.put("data", "verification code sent successfully");
            return res;
        } catch (Exception e) {
            Map<String, Object> err = Response.responseError();
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
        try {
            User user = userService.getPasswordResetToken(token);
            userService.updatePassword(user, newPassword);

            Map<String, Object> res = Response.responseOk();
            res.put("data", "verification code sent successfully");
            res.put("user", user);
            return res;
        } catch (Exception e) {
            Map<String, Object> err = Response.responseError();
            err.put("data", e.getMessage());
            return err;
        }
    }

}
