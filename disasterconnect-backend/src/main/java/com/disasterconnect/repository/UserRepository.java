package com.disasterconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.disasterconnect.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Used by Spring Security to find a user during login
    Optional<User> findByEmail(String email);

    // Existing method - loads User with Requests
    @Query("""
           SELECT DISTINCT u
           FROM User u
           LEFT JOIN FETCH u.requests
           WHERE u.id = :id
           """)
    User findUserWithRequestsById(Long id);
}