package com.example.cms.controllers;

import com.example.cms.models.User;
import com.example.cms.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    private final UserRepository userRepository;


    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    ResponseEntity<Iterable<User>> users() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
