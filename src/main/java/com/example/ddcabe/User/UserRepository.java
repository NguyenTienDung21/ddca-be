package com.example.ddcabe.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {

    // Finds a user by their username
    Optional<User> findByUsername(String username);

    // Finds a list of users by their role
    List<User> findByRole(String role);

}

