package com.example.contactmanager.user.service.impl;

import com.example.contactmanager.exception.ConflictException;
import com.example.contactmanager.exception.NotFoundException;
import com.example.contactmanager.user.dto.CreateUserRequest;
import com.example.contactmanager.user.dto.UpdateUserRequest;
import com.example.contactmanager.user.dto.UserResponse;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldCreateUserSuccessfully() {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("josh");
        request.setEmail("josh@email.com");
        request.setPassword("password");

        when(userRepository.findByUsername("josh"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("josh@email.com"))
                .thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("josh");
        savedUser.setEmail("josh@email.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("josh", response.getUsername());
        assertEquals("josh@email.com", response.getEmail());

        verify(userRepository).save(any(User.class));

    }

    @Test
    void createUser_ShouldThrowConflictException_WhenUsernameExists() {

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("josh");

        User existingUser = new User();

        when(userRepository.findByUsername("josh"))
                .thenReturn(Optional.of(existingUser));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.createUser(request)
        );

        assertEquals(
                "Username already exists: josh",
                exception.getErrorMessage()
        );

        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any());

    }

    @Test
    void createUser_ShouldThrowConflictException_WhenEmailExists() {

        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("josh@email.com");

        User existingUser = new User();

        when(userRepository.findByEmail("josh@email.com"))
                .thenReturn(Optional.of(existingUser));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.createUser(request)
        );

        assertEquals(
                "Email already exists: josh@email.com",
                exception.getErrorMessage()
        );

        verify(userRepository, never()).save(any());

    }

    @Test
    void getUserById_ShouldReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("josh");
        user.setEmail("josh@email.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertEquals(1L, response.getId());
        assertEquals("josh", response.getUsername());
        assertEquals("josh@email.com", response.getEmail());

    }

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(1L)
        );

    }

    @Test
    void deleteUser_ShouldDeleteUserSuccessfully() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);

    }

    @Test
    void deleteUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userRepository, never()).delete(any());

    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("oldUser");
        user.setEmail("old@email.com");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("newUser");
        request.setEmail("new@email.com");
        request.setPassword("newPassword");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByUsername("newUser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("new@email.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(userId, request);

        assertEquals("newUser", response.getUsername());
        assertEquals("new@email.com", response.getEmail());

        verify(userRepository).save(user);

    }

    @Test
    void updateUser_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest();

        assertThrows(
                NotFoundException.class,
                () -> userService.updateUser(1L, request)
        );

        verify(userRepository, never()).save(any());

    }

    @Test
    void updateUser_ShouldThrowConflictException_WhenUsernameAlreadyExists() {

        Long userId = 1L;

        User currentUser = new User();
        currentUser.setId(userId);

        User anotherUser = new User();
        anotherUser.setId(2L);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("existingUser");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(currentUser));

        when(userRepository.findByUsername("existingUser"))
                .thenReturn(Optional.of(anotherUser));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.updateUser(userId, request)
        );

        assertEquals(
                "Username already exists: existingUser",
                exception.getErrorMessage()
        );

        verify(userRepository, never()).save(any());

    }

    @Test
    void updateUser_ShouldThrowConflictException_WhenEmailAlreadyExists() {

        Long userId = 1L;

        User currentUser = new User();
        currentUser.setId(userId);

        User anotherUser = new User();
        anotherUser.setId(2L);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("existing@email.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(currentUser));

        request.setUsername("newUser");

        when(userRepository.findByUsername("newUser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("existing@email.com"))
                .thenReturn(Optional.of(anotherUser));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.updateUser(userId, request)
        );

        assertEquals(
                "Email already exists: existing@email.com",
                exception.getErrorMessage()
        );

        verify(userRepository, never()).save(any());

    }

    @Test
    void updateUser_ShouldAllowSameUsernameForCurrentUser() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("josh");
        request.setEmail("josh@email.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(userRepository.findByUsername("josh"))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmail("josh@email.com"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponse response =
                userService.updateUser(userId, request);

        assertEquals("josh", response.getUsername());
    }

}