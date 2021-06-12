package com.example.cms.controllers;

import com.example.cms.models.User;
import com.example.cms.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UsersController {

    private final UserService userService;


    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    ResponseEntity<Iterable<User>> users() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/my-user")
    ResponseEntity<?> getUser() {
        try {
            User user = userService.getMyUser();
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Some server error occured, we're sorry");
        }    }

}
