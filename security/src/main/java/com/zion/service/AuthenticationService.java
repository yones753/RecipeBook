package com.zion.service;



import com.zion.controller.request.SignUpRequest;
import com.zion.controller.request.SigninRequest;
import com.zion.controller.response.JwtAuthenticationResponse;

public interface AuthenticationService {

    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}