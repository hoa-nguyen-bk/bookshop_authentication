package com.cybersoft.bookshop_authentication.controller;

import com.cybersoft.bookshop_authentication.entity.Users;
import com.cybersoft.bookshop_authentication.payload.request.AuthenticationRequest;
import com.cybersoft.bookshop_authentication.payload.request.SignUpRequest;
import com.cybersoft.bookshop_authentication.payload.response.BaseResponse;
import com.cybersoft.bookshop_authentication.services.AuthenticationServices;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/authen")
public class AuthenticationController {

    @Autowired
    private AuthenticationServices authenticationServices;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody AuthenticationRequest authenticationRequest) {
        // Logic for user login
        String token = authenticationServices.signIn(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        BaseResponse response = new BaseResponse();
        response.setCode(200);
        response.setMessage("Login successful");
        response.setData(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {

        Users user = authenticationServices.signUp(signUpRequest);
        BaseResponse response = new BaseResponse();
        response.setCode(201);
        response.setMessage("Registration successful");
        response.setData(user);

        return ResponseEntity.status(201).body(response);
    }
}
