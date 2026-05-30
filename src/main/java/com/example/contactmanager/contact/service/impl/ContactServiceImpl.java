package com.example.contactmanager.contact.service.impl;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.model.entity.Contact;
import com.example.contactmanager.contact.repository.ContactRepository;
import com.example.contactmanager.contact.service.ContactService;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl (ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactResponse createContact(CreateContactRequest request) {

        Contact contact = generateContact(request);

        Contact savedContact = saveContact(contact);

        return mapToResponse(savedContact);

    }

    private Contact generateContact(CreateContactRequest request) {

        Contact contact = new Contact();

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());

        return contact;

    }

    private Contact saveContact(Contact contact) {

        return contactRepository.save(contact);

    }

    private ContactResponse mapToResponse(Contact contact) {

        ContactResponse response = new ContactResponse();

        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setCreateAt(contact.getCreatedAt());
        response.setUpdatedAt(contact.getCreatedAt());

        return response;

    }

}
