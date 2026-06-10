package com.example.contactmanager.user.controller;

import com.example.contactmanager.user.dto.CreateUserRequest;
import com.example.contactmanager.user.dto.UpdateUserRequest;
import com.example.contactmanager.user.dto.UserResponse;
import com.example.contactmanager.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        UserResponse response = userService.getUserById(id);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateUser(id, request);

        return ResponseEntity.ok(response);

    }

}
