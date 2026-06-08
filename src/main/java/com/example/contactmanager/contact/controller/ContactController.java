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

    @GetMapping("/{userId}")
    public ResponseEntity<List<ContactResponse>> getContactByUserId(@PathVariable Long userId) {

        List<ContactResponse> response = contactService.getContactByUserId(userId);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactResponse>> getAllContacts() {

        List<ContactResponse> response = contactService.getAllContacts();

        return ResponseEntity.ok(response);

    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
//
//        contactService.deleteContact(id);
//
//        return ResponseEntity.noContent().build();
//
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ContactResponse> updateContact(@PathVariable Long id, @Valid @RequestBody UpdateContactRequest request) {
//
//        ContactResponse response = contactService.updateContact(id, request);
//
//        return ResponseEntity.ok(response);
//
//    }

}
