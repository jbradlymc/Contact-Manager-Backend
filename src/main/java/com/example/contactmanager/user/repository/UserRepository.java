package com.example.contactmanager.user.repository;

import com.example.contactmanager.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {



}
