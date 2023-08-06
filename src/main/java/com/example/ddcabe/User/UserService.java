package com.example.ddcabe.User;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    /**
     * Constructs a UserService instance.
     *
     * @param userRepository the repository for user data access
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new user with the specified username, password, and role.
     *
     * @param account the user to add
     * @return the created user
     */
    public User addUser(User account) {
        if (checkUsername(account.getUsername())) {
            return null;
        }
        //Create the hashed password
        //Value 6 is the number of round that it will be hashed, the larger the round, the longer it takes to hash
        String hashedPassword = BCrypt.hashpw(account.getPassword(),BCrypt.gensalt(6));
        //Replace the user plain text password with the hashed one
        account.setPassword(hashedPassword);
        //Save data and return values
        return userRepository.save(account);
    }

    /**
     * Retrieves a list of users with the specified role.
     *
     * @return a list of users except Supervisor Role
     */
    public List<User> getAllUsers() {
        //get all users except supervisor
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.getRole().equals("Supervisor"));
        return users;
    }

    /**
     * Finds a user with the specified username.
     *
     * @param username the username of the user to find
     * @return an optional containing the found user, or empty if not found
     */
    public Optional<User> findByUsername(String username) {
        LOGGER.log(Level.FINE,"Inside FindBy Username");
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        LOGGER.log(Level.FINE,"check user: " + optionalUser.get());
        return userRepository.findByUsername(username);
    }

    /**
     * Deletes a user with the specified username.
     *
     * @param username the username of the user to delete
     * @return true if the user was successfully deleted, false otherwise
     */
    public boolean deleteUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        }
        return false;
    }

    public List<User> getAllOperators(String role) {
        return userRepository.findByRole(role);
    }

    public boolean checkUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }


}

