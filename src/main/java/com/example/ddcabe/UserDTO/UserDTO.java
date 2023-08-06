package com.example.ddcabe.UserDTO;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String name;
}
