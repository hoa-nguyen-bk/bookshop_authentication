package com.cybersoft.bookshop_authentication.services;

import com.cybersoft.bookshop_authentication.entity.Users;
import com.cybersoft.bookshop_authentication.payload.request.DecodeTokenRequest;
import com.cybersoft.bookshop_authentication.payload.request.SignUpRequest;

import java.util.List;

public interface AuthenticationServices {
    String signIn(String email, String password);

    List<String> decodeToken(String token);

    Users signUp(SignUpRequest signUpRequest);

}
