package com.example.contactmanager.contact.repository;

import com.example.contactmanager.contact.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhoneNumber(String phoneNumber);

}
