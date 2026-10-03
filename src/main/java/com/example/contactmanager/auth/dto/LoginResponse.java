package com.example.contactmanager.auth.dto;

import java.util.Date;

public class LoginResponse {

    private String token;
    private String tokenType;
    private Date expiresAt;

    public LoginResponse(String token,
                         String tokenType,
                         Date expiresAt) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }
}
