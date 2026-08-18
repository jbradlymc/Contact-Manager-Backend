package com.example.contactmanager.auth.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private Long expiresIn;

    public LoginResponse(String token,
                         String tokenType,
                         Long expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
