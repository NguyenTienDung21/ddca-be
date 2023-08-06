package com.example.ddcabe.example.ddcabe.SessionTest;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.Session.SessionRepository;
import com.example.ddcabe.Session.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionService = new SessionService(sessionRepository);
    }

    @Test
    void addSession_shouldSaveSession() {
        Session session = new Session();
        sessionService.addSession(session);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void getAllSessions_shouldReturnAllSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.getAllSessions();

        assertEquals(sessions, result);
    }

    @Test
    void getSessionByName_shouldReturnSessionByName() {
        String sessionName = "TestSession";
        Session session = new Session();
        when(sessionRepository.findByName(sessionName)).thenReturn(session);

        Session result = sessionService.getSessionByName(sessionName);

        assertEquals(session, result);
    }

    @Test
    void getSessionsByOperatorId_shouldReturnSessionsByOperatorId() {
        UUID operatorId = UUID.randomUUID();
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());
        when(sessionRepository.findByUserId(operatorId)).thenReturn(sessions);

        List<Session> result = sessionService.getSessionsByOperatorId(operatorId);

        assertEquals(sessions, result);
    }

    @Test
    void deleteSessionByName_shouldDeleteSessionByName() {
        String sessionName = "TestSession";
        sessionService.deleteSessionByName(sessionName);
        verify(sessionRepository, times(1)).deleteByName(sessionName);
    }


}
