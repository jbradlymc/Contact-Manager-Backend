package com.example.contactmanager.contact.controller;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.dto.UpdateContactRequest;
import com.example.contactmanager.contact.service.ContactService;
import com.example.contactmanager.contact.service.impl.ContactServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController (ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/createContact")
    public ResponseEntity<ContactResponse> createContact(@Valid @RequestBody CreateContactRequest request) {

        ContactResponse response = contactService.createContact(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{userId}/{id}")
    public ResponseEntity<ContactResponse> getContactByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {

        ContactResponse response = contactService.getContactByIdAndUserId(id, userId);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ContactResponse>> getContactByUserId(@PathVariable Long userId) {

        List<ContactResponse> response = contactService.getContactByUserId(userId);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id, @PathVariable Long userId) {

        contactService.deleteContact(id, userId);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{userId}/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long userId, @PathVariable Long id, @Valid @RequestBody UpdateContactRequest request) {

        ContactResponse response = contactService.updateContact(id, userId, request);

        return ResponseEntity.ok(response);

    }

}
