package com.example.contactmanager.security;

import java.util.Date;

public interface JwtService {

    String generateToken(String username);
    String extractUsername(String token);
    Date extractExpiration(String token);
    boolean isTokenValid(String token, String username);

}
