package com.example.contactmanager.contact.service.impl;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.dto.UpdateContactRequest;
import com.example.contactmanager.contact.model.entity.Contact;
import com.example.contactmanager.contact.repository.ContactRepository;
import com.example.contactmanager.contact.service.ContactService;
import com.example.contactmanager.exception.ConflictException;
import com.example.contactmanager.exception.NotFoundException;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactServiceImpl (ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ContactResponse createContact(CreateContactRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found with id: " + request.getUserId(),
                        Collections.emptyMap()
                ));

        if (contactRepository.findByUserIdAndEmail(user.getId(), request.getEmail()).isPresent()) {

            logger.warn(
                    "Contact with email already exists! email={}",
                    request.getEmail()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Contact with email already exists: " + request.getEmail(),
                    Collections.emptyMap()
            );
        }

        if (contactRepository.findByUserIdAndPhoneNumber(user.getId(), request.getPhoneNumber()).isPresent()) {

            logger.warn(
                    "Contact with phone number already exists! phoneNumber={}",
                    request.getPhoneNumber()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Contact with phone number already exists: " + request.getPhoneNumber(),
                    Collections.emptyMap()
            );
        }

        Contact contact = generateContact(request);

        contact.setUser(user);

        Contact savedContact = contactRepository.save(contact);

        logger.info(
                "Contact created successfully! contactId={}",
                savedContact.getId()
        );

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

    private ContactResponse mapToResponse(Contact contact) {

        ContactResponse response = new ContactResponse();

        response.setId(contact.getId());
        response.setUserId(contact.getUser().getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setCreateAt(contact.getCreatedAt());
        response.setUpdatedAt(contact.getUpdatedAt());

        return response;

    }

    @Override
    public ContactResponse getContactByIdAndUserId(Long id, Long userId) {

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "Contact not found for id: " + id +
                        " for user with userId: " + userId,
                        Collections.emptyMap()
                ));

        logger.info("userId={}", contact.getUser().getId());
        logger.info("Contact retrieved successfully! contactId={}", contact.getId());

        return mapToResponse(contact);

    }

    @Override
    public List<ContactResponse> getContactByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {

            logger.warn(
                    "User not found with id: {}",
                    userId
            );

            throw new NotFoundException(
                    HttpStatus.NOT_FOUND.value(),
                    "User not found with id: " + userId,
                    Collections.emptyMap()
            );
        }

        List<Contact> contacts = contactRepository.findByUserId(userId);

        logger.info("Contacts retrieved successfully for {}", userId);

        return contacts.stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public void deleteContact(Long id, Long userId) {

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "Contact not found for id: " + id +
                        " for user with userId: " + userId,
                        Collections.emptyMap()
                ));

        logger.info("userId={}", contact.getUser().getId());
        logger.info("Contact deleted successfully!' contactId={}", contact.getId());

        contactRepository.delete(contact);

    }

    @Override
    public ContactResponse updateContact(Long id, Long userId, UpdateContactRequest request) {

        Contact contact = contactRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "Contact not found with id: " + id +
                        " for user with userId: " + userId,
                        Collections.emptyMap()
                ));

        Optional<Contact> emailContact = contactRepository.findByUserIdAndEmail(userId, request.getEmail());

        if (emailContact.isPresent() && !emailContact.get().getId().equals(id)) {

            logger.warn(
                    "Contact with email already exists! email={}",
                    request.getEmail()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Contact with email already exists: " + request.getEmail(),
                    Collections.emptyMap()
            );

        }

        Optional<Contact> phoneContact = contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber());

        if (phoneContact.isPresent() && !phoneContact.get().getId().equals(id)) {

            logger.warn(
                    "Contact with phone number already exists! phoneNumber={}",
                    request.getPhoneNumber()
            );

            throw new ConflictException(
                    HttpStatus.CONFLICT.value(),
                    "Contact with phone number already exists: " + request.getPhoneNumber(),
                    Collections.emptyMap()
            );

        }

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());

        Contact updatedContact = contactRepository.save(contact);

        logger.info(
                "Contact updated successfully! contactId={}",
                updatedContact.getId()
        );

        return mapToResponse(updatedContact);

    }

}
