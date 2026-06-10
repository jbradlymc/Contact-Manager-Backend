package com.example.contactmanager.user.service;

import com.example.contactmanager.user.dto.CreateUserRequest;
import com.example.contactmanager.user.dto.UpdateUserRequest;
import com.example.contactmanager.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    void deleteUser(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

}
