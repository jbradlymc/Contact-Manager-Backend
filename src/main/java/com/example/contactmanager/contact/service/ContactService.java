package com.example.contactmanager.contact.service;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.dto.UpdateContactRequest;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

    ContactResponse getContactById(Long id);
    List<ContactResponse> getAllContacts();

    void deleteContact(Long id);

    ContactResponse updateContact(Long id, UpdateContactRequest request);

}
