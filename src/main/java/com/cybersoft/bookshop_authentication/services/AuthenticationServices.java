package com.cybersoft.bookshop_authentication.services;

import com.cybersoft.bookshop_authentication.entity.Users;
import com.cybersoft.bookshop_authentication.payload.request.SignUpRequest;

public interface AuthenticationServices {
    String signIn(String email, String password);
    Users signUp(SignUpRequest signUpRequest);
}
