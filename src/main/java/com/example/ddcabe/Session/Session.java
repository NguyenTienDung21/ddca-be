package com.example.ddcabe.Session;

import com.example.ddcabe.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * The Session class represents a session entity in the database.
 * <p>
 * It is mapped to the "sessions" table in the database.
 */
@Entity
@Table(name = "sessions")
@NoArgsConstructor
public class Session {

    /**
     * The ID of the session.
     * It is generated using the "uuid2" strategy.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    /**
     * The name of the session.
     */
    @Column(name = "name")
    private String name;

    /**
     * The date and time when the session was created.
     */
    @Column(nullable = false)
    private Date createdAt;

    /**
     * The user associated with the session.
     * It is mapped by the "user_id" foreign key in the sessions table.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // Prevents infinite recursion when serializing
    private User user;

    public Session(String name, User admin) {
        this.name = name;
        createdAt = new Date();
        this.user = admin;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
