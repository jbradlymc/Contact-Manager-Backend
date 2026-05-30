package com.example.contactmanager.contact.service;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;

public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

}
