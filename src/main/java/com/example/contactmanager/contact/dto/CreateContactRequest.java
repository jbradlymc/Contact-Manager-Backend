package com.example.contactmanager.contact.dto;

import jakarta.validation.constraints.*;

public class CreateContactRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    @Pattern(
            regexp = "^[A-Za-z\\s'-]+$",
            message = "Name can only contain letters, spaces, apostrophes, and hyphens"
    )
    private String firstName;

    @Pattern(
            regexp = "^[A-Za-z\\s'-]*$",
            message = "Last name can only contain letters, spaces, apostrophes, and hyphens"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "Phone number must be 11 digits and start with 09"
    )
    private String phoneNumber;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
