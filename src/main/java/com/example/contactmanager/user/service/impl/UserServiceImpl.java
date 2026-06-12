package com.example.contactmanager.user.service.impl;

import com.example.contactmanager.exception.ConflictException;
import com.example.contactmanager.exception.NotFoundException;
import com.example.contactmanager.user.dto.CreateUserRequest;
import com.example.contactmanager.user.dto.UpdateUserRequest;
import com.example.contactmanager.user.dto.UserResponse;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import com.example.contactmanager.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (request.getUsername() != null &&
                userRepository.findByUsername(request.getUsername()).isPresent()) {

            logger.warn(
                    "Username already exists! username={}",
                    request.getUsername()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Username already exists: " + request.getUsername(),
                    Collections.emptyMap()
            );

        }

        if (request.getEmail() != null &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {

            logger.warn(
                    "Email already exists! email={}",
                    request.getEmail()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Email already exists: " + request.getEmail(),
                    Collections.emptyMap()
            );

        }

        User user = generateUser(request);

        User savedUser = userRepository.save(user);

        logger.info(
                "User created successfully! userId={}",
                savedUser.getId()
        );

        return mapToUserResponse(savedUser);

    }

    private User generateUser(CreateUserRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;

    }

    private UserResponse mapToUserResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;

    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with id: " + id,
                        Collections.emptyMap()
                ));

        logger.info("User retrieved successfully! userId={}", id);

        return mapToUserResponse(user);

    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with id: " + id,
                        Collections.emptyMap()
                ));

        logger.info(
                "User deleted successfully! userId={}",
                id
        );

        userRepository.delete(user);

    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with id: " + id,
                        Collections.emptyMap()
                ));

        Optional<User> existingUsernameUser = userRepository.findByUsername(request.getUsername());

        if (existingUsernameUser.isPresent() && !existingUsernameUser.get().getId().equals(id)) {

            logger.warn(
                    "Username already exists! username={}",
                    request.getUsername()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Username already exists: " + request.getUsername(),
                    Collections.emptyMap()
            );

        }

        Optional<User> existingEmailUser = userRepository.findByEmail(request.getEmail());

        if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(id)) {

            logger.warn(
                    "Email already exists! email={}",
                    request.getEmail()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Email already exists: " + request.getEmail(),
                    Collections.emptyMap()
            );

        }

//        if (request.getUsername() != null) {
//            user.setUsername(request.getUsername());
//        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User updatedUser = userRepository.save(user);

        logger.info(
                "User updated successfully! userId={}",
                id
        );

        return mapToUserResponse(updatedUser);

    }

}
