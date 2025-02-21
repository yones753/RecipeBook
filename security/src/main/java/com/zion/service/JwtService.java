package com.zion.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public interface JwtService {

    String extractUserName(String token);
    String  extractUserRole(String token);
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}