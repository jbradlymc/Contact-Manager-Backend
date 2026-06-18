package com.example.contactmanager.auth.service.impl;

import com.example.contactmanager.auth.dto.LoginRequest;
import com.example.contactmanager.auth.dto.LoginResponse;
import com.example.contactmanager.auth.service.AuthService;
import com.example.contactmanager.exception.InvalidCredentialsException;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        logger.info("Login attempt for identifier: {}", request.getIdentifier());

        User user = userRepository
                .findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> {

                    logger.warn(
                            "Login failed. User not found: {}",
                            request.getIdentifier()
                    );

                    return new InvalidCredentialsException(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Invalid username/email or password",
                            Collections.emptyMap()
                    );

                });

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {

            logger.warn(
                    "Login failed. Invalid password for user: {}",
                    request.getIdentifier()
            );

            throw new InvalidCredentialsException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid username/email or password",
                    Collections.emptyMap()
            );

        }

        logger.info("Login successful for user: {}", request.getIdentifier());

        return new LoginResponse("Login successful");

    }

}
