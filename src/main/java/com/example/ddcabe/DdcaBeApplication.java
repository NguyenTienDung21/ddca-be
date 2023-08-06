package com.example.ddcabe;

import com.example.ddcabe.User.User;
import com.example.ddcabe.User.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DdcaBeApplication {

    private final UserService userService;

    public DdcaBeApplication(UserService userService) {
        this.userService = userService;
    }


    // The main entry point of the application
    public static void main(String[] args) {
        SpringApplication.run(DdcaBeApplication.class, args);
    }


    //When application run, we add 1 supervisor account
    @Bean
    CommandLineRunner commandLineRunner () {
        return args -> {
            User Supervisor = new User("Intel","intel123","Supervisor");
            userService.addUser(Supervisor);
        };
    }
}
