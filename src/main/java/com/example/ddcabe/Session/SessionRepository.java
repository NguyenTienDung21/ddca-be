package com.example.ddcabe.Session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session, UUID> {
    // Deletes all sessions created before the given date
    void deleteByCreatedAtBefore(LocalDateTime createdAt);

    // Retrieves a session by its name
    Session findByName(String sessionName);

    // Retrieves all sessions associated with a specific user ID
    List<Session> findByUserId(UUID operatorId);

    // Deletes a session by its name
    void deleteByName(String sessionName);
}

