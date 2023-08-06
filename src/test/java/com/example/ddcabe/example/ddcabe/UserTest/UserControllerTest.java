package com.example.ddcabe.example.ddcabe.UserTest;

import com.example.ddcabe.HttpResponse.ResponseBody;
import com.example.ddcabe.HttpResponse.ResponseError;
import com.example.ddcabe.Service.JWTAuthenticationService;
import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserController;
import com.example.ddcabe.User.UserService;
import com.example.ddcabe.UserDTO.UserDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserControllerTest {

    @Mock
    private JWTAuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", "password1", "Admin"));
        users.add(new User("user2", "password2", "Operator"));

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertEquals(users, result);
        verify(userService, times(1)).getAllUsers();
    }

    

    @Test
    void testAddUser() {
        User user = new User("user1", "password1", "role1");

        when(userService.addUser(user)).thenReturn(user);

        ResponseEntity<String> response = userController.addUser(user);
        String result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added successfully", result);
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void testAddUser_UsernameExists() {
        User user = new User("user1", "password1", "role1");

        when(userService.addUser(user)).thenReturn(null);

        ResponseEntity<String> response = userController.addUser(user);
        String result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Username already exists", result);
        verify(userService, times(1)).addUser(user);
    }

    @Test
    void testLoginUser_UserFound() {
        User user = new User("user1", "password1", "role1");
        String loginAccessToken = "access_token";

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(authenticationService.generateToken(user)).thenReturn(loginAccessToken);

        ResponseEntity<?> response = userController.loginUser(user);
        ResponseBody responseBody = (ResponseBody) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginAccessToken, responseBody.getData());
        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(authenticationService, times(1)).generateToken(user);
    }

    @Test
    void testLoginUser_UserNotFound() {
        User user = new User("user1", "password1", "role1");

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.loginUser(user);
        String result = (String) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid username or password", result);
        verify(userService, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testDeleteUser_UserDeleted() {
        String username = "user1";

        when(userService.deleteUserByUsername(username)).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUserByUsername(username);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String username = "user1";

        when(userService.deleteUserByUsername(username)).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser(username);
        ResponseError errorResponse = (ResponseError) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Operator not found", errorResponse.getMsg());
        assertEquals(404, errorResponse.getCode());
        verify(userService, times(1)).deleteUserByUsername(username);
    }


}
