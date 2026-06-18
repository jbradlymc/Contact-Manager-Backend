package com.example.contactmanager.auth.service;

import com.example.contactmanager.auth.dto.LoginRequest;
import com.example.contactmanager.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
