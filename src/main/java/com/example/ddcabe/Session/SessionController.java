package com.example.ddcabe.Session;

import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
@CrossOrigin(origins = "*")
@Slf4j
public class SessionController {

    private final SessionService sessionService;
    private final UserService userService;

    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     * Add a session.
     *
     * @param adminName the name of the admin
     */
    @PostMapping("/{adminName}/add")
    public void addSession(@PathVariable String adminName, @RequestParam(name = "name") String sessionName) {
        System.out.println("addSession: " + sessionName);
        User admin = userService.findByUsername(adminName).orElse(null);
        Session session = new Session(sessionName, admin);
        sessionService.addSession(session);
    }

    /**
     * Get all sessions.
     *
     * @return a list of all sessions
     */
    @GetMapping("/all")
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{operatorName}")
    public List<Session> getSessionsByOperatorId(@PathVariable String operatorName) {
        Optional<User> operator = userService.findByUsername(operatorName);
        return operator.map(user -> sessionService.getSessionsByOperatorId(user.getId())).orElse(null);
    }

    //delete Session by name
    @DeleteMapping("/delete")
    public void deleteSessionByName(@RequestBody Session session) {
        sessionService.deleteSessionByName(session.getName());
    }
}

