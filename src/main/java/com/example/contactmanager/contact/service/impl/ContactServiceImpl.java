package com.example.contactmanager.contact.service.impl;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.model.entity.Contact;
import com.example.contactmanager.contact.repository.ContactRepository;
import com.example.contactmanager.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;

    public ContactServiceImpl (ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactResponse createContact(CreateContactRequest request) {

        Contact contact = generateContact(request);

        Contact savedContact = saveContact(contact);

        logger.info("message='Contact created successfully!' contactId={}", savedContact.getId());

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

    @Override
    public ContactResponse getContactById(Long id) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        logger.info("message='Contact retrieved successfully!' contactId={}", contact.getId());

        return mapToResponse(contact);

    }

    @Override
    public List<ContactResponse> getAllContacts() {

        List<Contact> contact = contactRepository.findAll();

        logger.info("message='All contacts retrieved successfully!' totalContacts={}", contact.size());

        return contact.stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public ContactResponse deleteContact(Long id) {

            Contact contact = contactRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

            contactRepository.delete(contact);

            logger.info("message='Contact deleted successfully!' contactId={}", contact.getId());

            return mapToResponse(contact);

    }

}
