package com.example.cms.controllers;

import com.example.cms.exceptions.NoSuchConferenceException;
import com.example.cms.exceptions.UserUnauthorizedException;
import com.example.cms.models.Author;
import com.example.cms.models.Conference;
import com.example.cms.models.User;
import com.example.cms.repositories.AuthorRepository;
import com.example.cms.repositories.ConferenceRepository;
import com.example.cms.services.ConferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
class ConferenceController {

    private final ConferenceRepository conferenceRepository;
    private final AuthorRepository authorsRepository;
    private final ConferenceService conferenceService;

    @GetMapping("/")
    String hello() {
        return "Hello there";
    }

   @GetMapping("/conferences")
   Iterable<Conference> conferences() {
       return conferenceService.getAllConferences();
   }
    
//     @GetMapping("/conferences")
//     Iterable<Conference> searchByfilter(@RequestParam Optional<String> search
//     									@RequestParam Optional<String> sortBy) {
//         return conferenceRepository.findByName(search.orElse("_"),
//         		new PageRequest(page.orElse(0),5,
//         				Sort.Direction.ASC, sortBy.orElse("startDate")));
//     }
    
    @PostMapping("/conference")
    ResponseEntity<Conference> createConference(@RequestBody Conference conferenceToAdd) {
        Conference result = conferenceService.createConference(conferenceToAdd);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/conference/{id}")
    ResponseEntity<?> editConference(@PathVariable long id, @RequestBody Conference updated) {
        try {
            Conference conferenceToUpdate;
            conferenceToUpdate = conferenceService.updateConference(id, updated);
            return ResponseEntity.ok(conferenceToUpdate);
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        } catch (UserUnauthorizedException e) {
            return ResponseEntity.status(401).body("You must be the author to update a conference.");
        }
    }

    @DeleteMapping("/conference/{id}")
    ResponseEntity<?> deleteConference(@PathVariable long id) {
        try {
            conferenceService.deleteConference(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        } catch (UserUnauthorizedException e) {
            return ResponseEntity.status(401).body("You must be the author to delete a conference.");
        }
    }

    ConferenceController(ConferenceRepository conferenceRepository, AuthorRepository authorsRepository, ConferenceService conferenceService) {
        this.conferenceRepository = conferenceRepository;
        this.authorsRepository = authorsRepository;
        this.conferenceService = conferenceService;
    }
}
