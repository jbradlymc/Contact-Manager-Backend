package com.example.contactmanager.contact.service.impl;

import com.example.contactmanager.contact.dto.ContactResponse;
import com.example.contactmanager.contact.dto.CreateContactRequest;
import com.example.contactmanager.contact.dto.UpdateContactRequest;
import com.example.contactmanager.contact.model.entity.Contact;
import com.example.contactmanager.contact.repository.ContactRepository;
import com.example.contactmanager.exception.ConflictException;
import com.example.contactmanager.exception.NotFoundException;
import com.example.contactmanager.user.model.entity.User;
import com.example.contactmanager.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    //================= CREATE CONTACT =====================

    @Test
    void createContact_ShouldReturnContactResponse_WhenRequestIsValid() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        CreateContactRequest request = new CreateContactRequest();
        request.setUserId(userId);
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPhoneNumber("09123456789");

        Contact savedContact = new Contact();
        savedContact.setId(100L);
        savedContact.setUser(user);
        savedContact.setFirstName("John");
        savedContact.setLastName("Doe");
        savedContact.setEmail("john@example.com");
        savedContact.setPhoneNumber("09123456789");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.empty());

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.empty());

        when(contactRepository.save(any(Contact.class)))
                .thenReturn(savedContact);

        ContactResponse response = contactService.createContact(request);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(userId, response.getUserId());

        ArgumentCaptor<Contact> contactCaptor =
                ArgumentCaptor.forClass(Contact.class);

        verify(contactRepository).save(contactCaptor.capture());

        Contact capturedContact = contactCaptor.getValue();

        assertEquals("John", capturedContact.getFirstName());
        assertEquals("Doe", capturedContact.getLastName());
        assertEquals("john@example.com", capturedContact.getEmail());
        assertEquals("09123456789", capturedContact.getPhoneNumber());
        assertEquals(user, capturedContact.getUser());

    }

    @Test
    void createContact_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        Long userId = 1L;

        CreateContactRequest request = new CreateContactRequest();
        request.setUserId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> contactService.createContact(request)
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        assertEquals(
                "User not found with id: 1",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).findByUserIdAndEmail(anyLong(), anyString());

        verify(contactRepository, never()).findByUserIdAndPhoneNumber(anyLong(), anyString());

        verify(contactRepository, never()).save(any());

    }

    @Test
    void createContact_ShouldThrowConflictException_WhenEmailAlreadyExists() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        CreateContactRequest request = new CreateContactRequest();
        request.setUserId(userId);
        request.setEmail("john@example.com");

        Contact existingContact = new Contact();
        existingContact.setId(10L);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.of(existingContact));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> contactService.createContact(request)
        );

        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorCode());
        assertEquals(
                "Contact with email already exists: john@example.com",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).findByUserIdAndPhoneNumber(anyLong(), anyString());

        verify(contactRepository, never()).save(any());

    }

    @Test
    void createContact_ShouldThrowConflictException_WhenPhoneNumberAlreadyExists() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        CreateContactRequest request = new CreateContactRequest();
        request.setUserId(userId);
        request.setEmail("john@example.com");
        request.setPhoneNumber("09123456789");

        Contact existingContact = new Contact();
        existingContact.setId(10L);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.empty());

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.of(existingContact));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> contactService.createContact(request)
        );

        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorCode());
        assertEquals(
                "Contact with phone number already exists: 09123456789",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).save(any());

    }

    //================= GET CONTACT =====================

    @Test
    void getContactByIdAndUserId_ShouldReturnContactResponse_WhenContactExists() {

        Long contactId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setUser(user);

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(contact));

        ContactResponse response =
                contactService.getContactByIdAndUserId(contactId, userId);

        assertEquals(contactId, response.getId());
        assertEquals(userId, response.getUserId());

    }

    @Test
    void getContactByIdAndUserId_ShouldThrowNotFoundException_WhenContactDoesNotExist() {

        Long contactId = 1L;
        Long userId = 1L;

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> contactService.getContactByIdAndUserId(contactId, userId)
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        assertEquals(
                "Contact not found for id: 1 for user with userId: 1",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

    }

    @Test
    void getContactByUserId_ShouldReturnContacts_WhenUserExists() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(10L);
        contact.setUser(user);

        when(userRepository.existsById(userId))
                .thenReturn(true);

        when(contactRepository.findByUserId(userId))
                .thenReturn(List.of(contact));

        List<ContactResponse> responses =
                contactService.getContactByUserId(userId);

        assertEquals(1, responses.size());
        assertEquals(10L, responses.get(0).getId());

    }

    @Test
    void getContactByUserId_ShouldThrowNotFoundException_WhenUserDoesNotExist() {

        Long userId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> contactService.getContactByUserId(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        assertEquals(
                "User not found with id: 1",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).findByUserId(anyLong());

    }

    @Test
    void getContactByUserId_ShouldReturnEmptyList_WhenUserHasNoContacts() {

        Long userId = 1L;

        when(userRepository.existsById(userId))
                .thenReturn(true);

        when(contactRepository.findByUserId(userId))
                .thenReturn(List.of());

        List<ContactResponse> responses =
                contactService.getContactByUserId(userId);

        assertTrue(responses.isEmpty());
    }

    //================= DELETE CONTACT =====================

    @Test
    void deleteContact_ShouldDeleteContact_WhenContactExists() {

        Long contactId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setUser(user);

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(contact));

        contactService.deleteContact(contactId, userId);

        verify(contactRepository).delete(contact);

    }

    @Test
    void deleteContact_ShouldThrowNotFoundException_WhenContactDoesNotExist() {

        Long contactId = 1L;
        Long userId = 1L;

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> contactService.deleteContact(contactId, userId)
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        assertEquals(
                "Contact not found for id: 1 for user with userId: 1",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).delete(any());

    }

    //================= UPDATE CONTACT =====================

    @Test
    void updateContact_ShouldReturnUpdatedContact_WhenRequestIsValid() {

        Long contactId = 1L;
        Long userId = 100L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setUser(user);
        contact.setFirstName("Old");
        contact.setLastName("Name");
        contact.setEmail("old@email.com");
        contact.setPhoneNumber("09111111111");

        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@email.com");
        request.setPhoneNumber("09222222222");

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(contact));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.empty());

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.empty());

        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContactResponse response =
                contactService.updateContact(contactId, userId, request);

        assertNotNull(response);
        assertEquals(contactId, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john@email.com", response.getEmail());
        assertEquals("09222222222", response.getPhoneNumber());

        ArgumentCaptor<Contact> contactCaptor =
                ArgumentCaptor.forClass(Contact.class);

        verify(contactRepository).save(contactCaptor.capture());

        Contact capturedContact = contactCaptor.getValue();

        assertEquals("John", capturedContact.getFirstName());
        assertEquals("Doe", capturedContact.getLastName());
        assertEquals("john@email.com", capturedContact.getEmail());
        assertEquals("09222222222", capturedContact.getPhoneNumber());

    }

    @Test
    void updateContact_ShouldThrowNotFoundException_WhenContactDoesNotExist() {

        Long contactId = 1L;
        Long userId = 100L;

        UpdateContactRequest request = new UpdateContactRequest();

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> contactService.updateContact(contactId, userId, request)
        );

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
        assertEquals(
                "Contact not found with id: 1 for user with userId: 100",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).findByUserIdAndEmail(anyLong(), anyString());

        verify(contactRepository, never()).findByUserIdAndPhoneNumber(anyLong(), anyString());

        verify(contactRepository, never()).save(any());

    }

    @Test
    void updateContact_ShouldThrowConflictException_WhenEmailBelongsToAnotherContact() {

        Long contactId = 1L;
        Long userId = 100L;

        User user = new User();
        user.setId(userId);

        Contact currentContact = new Contact();
        currentContact.setId(contactId);
        currentContact.setUser(user);

        Contact anotherContact = new Contact();
        anotherContact.setId(999L);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setEmail("existing@email.com");
        request.setPhoneNumber("09123456789");

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(currentContact));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.of(anotherContact));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> contactService.updateContact(contactId, userId, request)
        );

        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorCode());
        assertEquals(
                "Contact with email already exists: existing@email.com",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).findByUserIdAndPhoneNumber(anyLong(), anyString());

        verify(contactRepository, never()).save(any());

    }

    @Test
    void updateContact_ShouldThrowConflictException_WhenPhoneNumberBelongsToAnotherContact() {

        Long contactId = 1L;
        Long userId = 100L;

        User user = new User();
        user.setId(userId);

        Contact currentContact = new Contact();
        currentContact.setId(contactId);
        currentContact.setUser(user);

        Contact anotherContact = new Contact();
        anotherContact.setId(999L);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setEmail("new@email.com");
        request.setPhoneNumber("09123456789");

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(currentContact));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.empty());

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.of(anotherContact));

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> contactService.updateContact(contactId, userId, request)
        );

        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorCode());
        assertEquals(
                "Contact with phone number already exists: 09123456789",
                exception.getErrorMessage()
        );
        assertTrue(exception.getErrorDetails().isEmpty());

        verify(contactRepository, never()).save(any());

    }

    @Test
    void updateContact_ShouldAllowSameEmail_WhenBelongsToCurrentContact() {

        Long contactId = 1L;
        Long userId = 100L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setUser(user);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setEmail("same@email.com");
        request.setPhoneNumber("09222222222");

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(contact));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.of(contact));

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.empty());

        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContactResponse response =
                contactService.updateContact(contactId, userId, request);

        assertNotNull(response);

        verify(contactRepository).save(contact);

    }

    @Test
    void updateContact_ShouldAllowSamePhoneNumber_WhenBelongsToCurrentContact() {

        Long contactId = 1L;
        Long userId = 100L;

        User user = new User();
        user.setId(userId);

        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setUser(user);

        UpdateContactRequest request = new UpdateContactRequest();
        request.setEmail("new@email.com");
        request.setPhoneNumber("09123456789");

        when(contactRepository.findByIdAndUserId(contactId, userId))
                .thenReturn(Optional.of(contact));

        when(contactRepository.findByUserIdAndEmail(userId, request.getEmail()))
                .thenReturn(Optional.empty());

        when(contactRepository.findByUserIdAndPhoneNumber(userId, request.getPhoneNumber()))
                .thenReturn(Optional.of(contact));

        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContactResponse response =
                contactService.updateContact(contactId, userId, request);

        assertNotNull(response);

        verify(contactRepository).save(contact);

    }

}