package com.bitbubble.api.app.contollers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitbubble.api.app.data.Requests.JwtRequest;
import com.bitbubble.api.app.data.Requests.RefreshRequest;
import com.bitbubble.api.app.data.Response.JwtResponse;
import com.bitbubble.api.app.data.Response.Response;
import com.bitbubble.api.app.entitiy.RefreshToken;
import com.bitbubble.api.app.entitiy.User;
import com.bitbubble.api.app.service.JwtService;
import com.bitbubble.api.app.service.RefreshTokenService;
import com.bitbubble.api.app.util.jwtutil;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin
public class JwtController {
        @Autowired
        private JwtService jwtService;

        @Autowired
        private jwtutil jwtutil;

        @Autowired
        RefreshTokenService refreshTokenService;

        @PostMapping({ "/authenticate" })
        public Map<String, Object> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
                Response response =  new Response();
                try {
                        JwtResponse data = jwtService.createJwtResponse(jwtRequest);
                        Claims claims = jwtutil.getAllClaimsFromToken(data.getJwtToken());
                        
                        Map<String, Object> res =  response.responseOk();
                        res.put("data", data);
                        res.put("payload", claims);
                        return res;

                } catch (Exception e) {
                        Map<String, Object> err = response.responseError();
                        err.put("data", "invalid email or password");
                        return err;
                }

        }

        @PostMapping({ "/refresh" })
        public Map<String, Object> refreshUserToken(@RequestBody RefreshRequest token) {
                Response response = new Response();
                try {
                        Optional<RefreshToken> ref = refreshTokenService.findByToken(token);
                        RefreshToken verify = refreshTokenService.verifyExpiration(ref.get());
                        User user = verify.getUser();
                       
                        String newToken = jwtutil.generateTokenByName(user);
                        Claims claims = jwtutil.getAllClaimsFromToken(newToken);
                        Map<String, Object> res = response.responseOk();
                        res.put("newToken", newToken);
                        res.put("payload", claims);
                        return res;

                } catch (Exception e) {
                        Map<String, Object> err = response.responseError();
                        err.put("data", e.getMessage());
                        return err;
                }
        }
}
