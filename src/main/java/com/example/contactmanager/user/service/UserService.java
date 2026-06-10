package com.example.contactmanager.user.service;

import com.example.contactmanager.user.dto.CreateUserRequest;
import com.example.contactmanager.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

}
