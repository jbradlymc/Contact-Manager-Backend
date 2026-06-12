package com.example.contactmanager.contact.repository;

import com.example.contactmanager.contact.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByUserId(Long userId);
    Optional<Contact> findByIdAndUserId(Long id, Long userId);
    Optional<Contact> findByUserIdAndEmail(Long userId, String email);
    Optional<Contact> findByUserIdAndPhoneNumber(Long userId, String phoneNumber);

}
