package com.example.ddcabe.Session;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository; // Repository for session-related operations

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Adds a session to the session repository.
     *
     * @param session the session to be added
     */
    public void addSession(Session session) {
        sessionRepository.save(session);
    }

    /**
     * Retrieves all sessions from the session repository.
     *
     * @return a list of Session objects
     */
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    /**
     * Retrieves a session by its name from the session repository.
     *
     * @param sessionName the name of the session
     * @return a Session object if found, otherwise null
     */
    public Session getSessionByName(String sessionName) {
        return sessionRepository.findByName(sessionName);
    }

    /**
     * Retrieves sessions associated with a specific operator ID from the session repository.
     *
     * @param operatorId the ID of the operator
     * @return a list of Session objects
     */
    public List<Session> getSessionsByOperatorId(UUID operatorId) {
        return sessionRepository.findByUserId(operatorId);
    }

    /**
     * Deletes a session by its name from the session repository.
     *
     * @param sessionName the name of the session
     */
    public void deleteSessionByName(String sessionName) {
    	sessionRepository.deleteByName(sessionName);
    }
}

