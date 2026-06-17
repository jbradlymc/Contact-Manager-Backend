package com.example.contactmanager.auth.service.impl;

import com.example.contactmanager.auth.dto.LoginRequest;
import com.example.contactmanager.auth.service.AuthService;
import com.example.contactmanager.exception.InvalidCredentialsException;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(
                        request.getIdentifier(),
                        request.getIdentifier()
                )
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid username/email or password"
                ));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "Invalid username/email or password"
            );
        }

    }

}
