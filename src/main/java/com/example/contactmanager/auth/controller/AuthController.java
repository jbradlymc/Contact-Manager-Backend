package com.example.contactmanager.auth.controller;

import com.example.contactmanager.auth.dto.LoginRequest;
import com.example.contactmanager.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {

        authService.login(request);

        return ResponseEntity.ok("Login successful");

    }

}
