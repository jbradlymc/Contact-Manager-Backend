package com.example.contactmanager.contact.service;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.dto.UpdateContactRequest;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

    ContactResponse getContactByIdAndUserId(Long id, Long userId);
    List<ContactResponse> getContactByUserId(Long userId);

    void deleteContact(Long id, Long userId);

    ContactResponse updateContact(Long id, Long userId, UpdateContactRequest request);

}
