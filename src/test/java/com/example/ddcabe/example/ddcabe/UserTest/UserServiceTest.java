package com.example.ddcabe.example.ddcabe.UserTest;

import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserRepository;
import com.example.ddcabe.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void testAddUser() {
        User user = new User("john", "password", "Admin");
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        verify(userRepository, times(1)).findByUsername("john");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddUser_UsernameExists() {
        User user = new User("john", "password", "user");
        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        User savedUser = userService.addUser(user);

        assertNull(savedUser);
        verify(userRepository, times(1)).findByUsername("john");
        verify(userRepository, never()).save(user);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("john", "password", "Admin"));
        users.add(new User("jane", "password", "Operator"));
        users.add(new User("jack", "password", "Supervisor"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertFalse(result.stream().anyMatch(user -> user.getRole().equals("Supervisor")));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByUsername() {
        User user = new User("john", "password", "Admin");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        verify(userRepository, times(2)).findByUsername("john");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            Optional<User> result = userService.findByUsername("john");
            assertFalse(result.isPresent());
            verify(userRepository, times(1)).findByUsername("john");
        });

    }

    @Test
    void testDeleteUserByUsername() {
        User user = new User("john", "password", "user");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        boolean deleted = userService.deleteUserByUsername("john");

        assertTrue(deleted);
        verify(userRepository, times(1)).findByUsername("john");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        boolean deleted = userService.deleteUserByUsername("john");

        assertFalse(deleted);
        verify(userRepository, times(1)).findByUsername("john");
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testGetAllOperators() {
        List<User> operators = new ArrayList<>();
        operators.add(new User("john", "password", "operator"));
        operators.add(new User("jane", "password", "operator"));
        when(userRepository.findByRole("operator")).thenReturn(operators);

        List<User> result = userService.getAllOperators("operator");

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByRole("operator");
    }

    @Test
    void testCheckUsername() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User("john", "password", "user")));

        boolean exists = userService.checkUsername("john");

        assertTrue(exists);
        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void testCheckUsername_UserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        boolean exists = userService.checkUsername("john");

        assertFalse(exists);
        verify(userRepository, times(1)).findByUsername("john");
    }

}
