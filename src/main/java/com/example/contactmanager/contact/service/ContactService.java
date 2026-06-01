package com.example.contactmanager.contact.service;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

    ContactResponse getContactById(Long id);
    List<ContactResponse> getAllContacts();

    ContactResponse deleteContact(Long id);

}
