package com.example.contactmanager.contact.controller;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.service.ContactService;
import com.example.contactmanager.contact.service.impl.ContactServiceImpl;
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
    public ResponseEntity<ContactResponse> createContact(@RequestBody CreateContactRequest request) {

        ContactResponse response = contactService.createContact(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {

        ContactResponse response = contactService.getContactById(id);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactResponse>> getAllContacts() {

        List<ContactResponse> response = contactService.getAllContacts();

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ContactResponse> deleteContact(@PathVariable Long id) {

        ContactResponse response = contactService.deleteContact(id);

        return ResponseEntity.noContent().build();

    }

}
