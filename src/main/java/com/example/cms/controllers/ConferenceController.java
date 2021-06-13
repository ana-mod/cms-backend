package com.example.cms.controllers;

import com.example.cms.exceptions.NoMatchingConferencesException;
import com.example.cms.exceptions.NoSuchConferenceException;
import com.example.cms.exceptions.UserUnauthorizedException;
import com.example.cms.models.Conference;
import com.example.cms.models.Presentation;
import com.example.cms.repositories.AuthorRepository;
import com.example.cms.repositories.ConferenceRepository;
import com.example.cms.services.ConferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
class ConferenceController {

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

    @GetMapping("/conferences/{id}")
    ResponseEntity<?> getConference(@PathVariable long id) {
        try {
            Conference conference = conferenceService.getConference(id);
            return ResponseEntity.ok(conference);
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        }
    }

    @GetMapping("/my-conferences")
    ResponseEntity<?> getMyConferences() {
        try {
            List<Conference> myConferences = conferenceService.getMyConferences();
            return ResponseEntity.ok(myConferences);
        } catch (NoMatchingConferencesException e) {
            return ResponseEntity.status(404).body("There are no conferences created by this user");
        }
    }


    @PostMapping("/conference")
    ResponseEntity<Conference> createConference(@RequestBody Conference conferenceToAdd) {
        Conference result = conferenceService.createConference(conferenceToAdd);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/conference/{id}")
    ResponseEntity<?> editConference(@PathVariable long id, @RequestBody Conference updated) {
        try {
            Conference conferenceToUpdate = conferenceService.updateConference(id, updated);
            return ResponseEntity.ok(conferenceToUpdate);
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        } catch (UserUnauthorizedException e) {
            return ResponseEntity.status(401).body("You must be the creator to update a conference.");
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
            return ResponseEntity.status(401).body("You must be the creator to delete a conference.");
        }
    }

    @GetMapping("/enroll/{id}")
    public ResponseEntity<?> enrollToConference(@PathVariable long id) {
        try {
            conferenceService.enrollToConference(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        }
    }

    @GetMapping("/disenroll/{id}")
    public ResponseEntity<?> disenrollFromConference(@PathVariable long id) {
        try {
            conferenceService.disenrollFromConference(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        }
    }

    @GetMapping("/participants/{id}")
    public ResponseEntity<?> getParticipants(@PathVariable long id) {
        try {
            return ResponseEntity.ok(conferenceService.getParticipants(id));
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        }
    }

    @GetMapping("/my-enrolled-conferences")
    public ResponseEntity<?> getEnrolledConferences(){
        try {
            return ResponseEntity.ok(conferenceService.getEnrolledConferences());
        } catch (NoMatchingConferencesException e) {
            return ResponseEntity.status(404).body("You are not enrolled to any conferences yet");
        }
    }

    @PostMapping("/conference/{id}/presentation")
    public ResponseEntity<?> addPresentationToExisting(@PathVariable long id, @RequestBody Presentation presentation) {

        try {
            Conference conferenceToAddPresentationTo = conferenceService.addPresentationToExisting(id, presentation);
            return ResponseEntity.ok(conferenceToAddPresentationTo);
        } catch (NoSuchConferenceException e) {
            return ResponseEntity.status(404).body("There is no such conference");
        } catch (UserUnauthorizedException e) {
            return ResponseEntity.status(401).body("You must be the creator to add a presentation.");
        }

    }

    ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }
}