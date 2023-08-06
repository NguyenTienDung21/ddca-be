package com.example.ddcabe.example.ddcabe.SessionTest;

import com.example.ddcabe.Session.Session;
import com.example.ddcabe.Session.SessionController;
import com.example.ddcabe.Session.SessionService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SessionControllerTest {
    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessionController = new SessionController(sessionService, userService);
    }
    

    @Test
    void getAllSessions_shouldReturnAllSessionsAndHttpStatusOk() {
        // Arrange
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("session1", new User("admin", "password", "Admin")));
        sessions.add(new Session("session2", new User("admin", "password", "Admin")));

        when(sessionService.getAllSessions()).thenReturn(sessions);

        // Act
        ResponseEntity<List<Session>> response = sessionController.getAllSessions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessions, response.getBody());
    }

    @Test
    void getSessionsByOperatorId_shouldReturnSessionsByOperatorIdAndHttpStatusOk() {
        // Arrange
        String operatorName = "operator";
        User operator = new User(operatorName, "password", "Operator");
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("session1", operator));
        sessions.add(new Session("session2", operator));

        when(userService.findByUsername(operatorName)).thenReturn(Optional.of(operator));
        when(sessionService.getSessionsByOperatorId(operator.getId())).thenReturn(sessions);

        // Act
        List<Session> result = sessionController.getSessionsByOperatorId(operatorName);

        // Assert
        assertEquals(sessions, result);
    }

    @Test
    void deleteSessionByName_shouldDeleteSessionAndReturnHttpStatusOk() {
        // Arrange
        Session session = new Session("session", new User("admin", "password", "Admin"));

        // Act
        sessionController.deleteSessionByName(session);

        // Assert
        verify(sessionService, times(1)).deleteSessionByName(session.getName());
    }
}
