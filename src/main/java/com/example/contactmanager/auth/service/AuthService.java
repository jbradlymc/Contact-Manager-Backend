package com.example.contactmanager.auth.service;

import com.example.contactmanager.auth.dto.LoginRequest;

public interface AuthService {

    void login(LoginRequest request);

}
