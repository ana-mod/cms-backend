package com.example.cms.controllers;

import com.example.cms.models.Author;
import com.example.cms.models.Conference;
import com.example.cms.models.User;
import com.example.cms.repositories.AuthorRepository;
import com.example.cms.repositories.ConferenceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ConferenceController {

    private final ConferenceRepository conferenceRepository;
    private final AuthorRepository authorsRepository;

    @GetMapping("/")
    String hello() {
        return "Hello there";
    }

    @GetMapping("/conferences")
    Iterable<Conference> conferences() {
        return conferenceRepository.findAll();
    }

    @PostMapping("/conference")
    ResponseEntity<Conference> createConference(@RequestBody Conference conferenceToAdd) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Author author;
        if (authorsRepository.existsByUserId(user.getId())) {
            author = authorsRepository.findByUserId(user.getId());
        } else {
            author = new Author(user);
            authorsRepository.save(author);
        }
        conferenceToAdd.setAuthor(author);

        Conference result = conferenceRepository.save(conferenceToAdd);
        return ResponseEntity.ok(result);
    }

    ConferenceController(ConferenceRepository conferenceRepository, AuthorRepository authorsRepository) {
        this.conferenceRepository = conferenceRepository;
        this.authorsRepository = authorsRepository;
    }
}