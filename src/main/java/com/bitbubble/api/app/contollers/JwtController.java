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
                try {
                        JwtResponse data = jwtService.createJwtResponse(jwtRequest);
                        Claims claims = jwtutil.getAllClaimsFromToken(data.getJwtToken());
                        Response response = new Response();
                        Map<String, Object> res =  response.responseOk();
                        res.put("data", data);
                        res.put("payload", claims);
                        return res;

                } catch (Exception e) {
                        Response response = new Response();
                        Map<String, Object> err = response.responseError();
                        err.put("data", e.getMessage());
                        return err;
                }

        }

        @PostMapping({ "/refresh" })
        public Map<String, Object> refreshUserToken(@RequestBody RefreshRequest token) {
                try {
                        Optional<RefreshToken> ref = refreshTokenService.findByToken(token);
                        RefreshToken verify = refreshTokenService.verifyExpiration(ref.get());
                        User user = verify.getUser();
                       
                        Response response = new Response();
                        String newToken = jwtutil.generateTokenByName(user);
                        Claims claims = jwtutil.getAllClaimsFromToken(newToken);
                        Map<String, Object> res = response.responseOk();
                        res.put("newToken", newToken);
                        res.put("payload", claims);
                        return res;

                } catch (Exception e) {
                        Response response = new Response();
                        Map<String, Object> err = response.responseError();
                        err.put("data", e.getMessage());
                        return err;
                }
        }
}
