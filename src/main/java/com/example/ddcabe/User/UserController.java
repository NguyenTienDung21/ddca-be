package com.example.ddcabe.User;

import com.example.ddcabe.HttpResponse.ResponseBody;
import com.example.ddcabe.HttpResponse.ResponseError;
import com.example.ddcabe.Service.JWTAuthenticationService;
import com.example.ddcabe.UserDTO.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    private final JWTAuthenticationService authenticationService;

    private final UserService userService;

    public UserController(JWTAuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test () {
        return "Test";
    }


    /**
     * Retrieves a list of users based on the specified role.
     *
     * @return A list of users except Supervisor Role.
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllOperators(@RequestParam("role") String role) {
        List<User> operators = userService.getAllOperators(role);

        // Create a list of UserDTO objects containing only the name and id fields
        List<UserDTO> operatorDTOs = new ArrayList<>();
        for (User operator : operators) {
            UserDTO operatorDTO = new UserDTO();
            operatorDTO.setId(operator.getId());
            operatorDTO.setName(operator.getUsername());
            operatorDTOs.add(operatorDTO);
        }

        return ResponseEntity.ok(operatorDTOs);
    }

    /**
     * Adds a new user with the specified username, password, and role.
     *
     * @param account The user to add.
     * @return The added user.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User account) {
        User user = userService.addUser(account);
        if (user == null) {
            return ResponseEntity.ok("Username already exists");
        }
        return ResponseEntity.ok("User added successfully");
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequest The user's login request containing the username and password.
     * @return If the login is successful, returns the user's role. Otherwise, returns an error message.
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        log.info("Start login api" + loginRequest.getUsername() + " " + loginRequest.getPassword());
        Optional<User> user = this.userService.findByUsername(loginRequest.getUsername());
        //If the user is found
        if (user.isPresent()) {
            log.info("User found in database: " + user);
            String loginAccessToken = authenticationService.generateToken(user.get());
            ResponseBody responseBody = new ResponseBody(loginAccessToken);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            log.info("user is not found");
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    /**
     * Deletes a user with the specified username.
     *
     * @param username The username of the user to delete.
     * @return If the user is deleted successfully, returns an OK response. Otherwise, returns an error response.
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        boolean deleted = userService.deleteUserByUsername(username);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            ResponseError errorResponse = new ResponseError("Operator not found", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
