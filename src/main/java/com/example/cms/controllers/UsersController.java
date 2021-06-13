package com.example.cms.controllers;

import com.example.cms.models.Conference;
import com.example.cms.models.Presentation;
import com.example.cms.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cms.services.ConferenceService;
import com.example.cms.services.UserService;

import java.util.LinkedList;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UsersController {


    private final UserService userService;
    private final ConferenceService conferenceService;

    public UsersController(UserService userService, ConferenceService conferenceService) {
        this.userService = userService;
        this.conferenceService = conferenceService;
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
            return ResponseEntity.status(500)
                    .body("Some server error occured while retrieving your account, we're sorry");
        }
    }

    @GetMapping("/users/addPresentation")
    ResponseEntity<?>addPresentation(@RequestBody Presentation presentation){
            LinkedList<Presentation> presentations = new LinkedList<Presentation>();

            presentations = (LinkedList<Presentation>) userService.getMyUser().getPresentations();
            presentations.add(presentation);
            userService.getMyUser().setPresentations(presentations);
            return  ResponseEntity.ok(presentation);
    }

    @DeleteMapping("/delete-acc")
    ResponseEntity<?> deleteUser() {

        try {
            conferenceService.disenrollFromEveryConference();
            userService.deleteUser();
            return ResponseEntity.ok().build();
        }

        catch (RuntimeException e) {
            return ResponseEntity.status(500)
                    .body("Some server error occured while deleting your account, we're sorry");
        }

    }
}
