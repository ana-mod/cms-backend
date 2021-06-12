package com.example.cms.controllers;

import com.example.cms.models.*;
import com.example.cms.repositories.AuthorRepository;
import com.example.cms.repositories.ConferenceRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
class ConferenceController {

    private final ConferenceRepository conferenceRepository;
    private final AuthorRepository authorsRepository;

    @GetMapping("/")
    String hello() {
        return "Hello there";
    }


    @GetMapping("/conferences")
    public List<Conference> findAll(@RequestParam Optional<String> search,
                                    @RequestParam Optional<Integer> page,
                                    @RequestParam Optional<String> sortBy){
        int pageOrElse = page.orElse(0);
        Pageable pageable = PageRequest.of(pageOrElse,5,Sort.by(sortBy.orElse("topic")));
        return conferenceRepository.findByName(search.orElse("_"), pageable);
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
    @PostMapping("/conference/{id}/addPresentation")
    ResponseEntity<?>addPresentationExistingConference(@PathVariable long id, @RequestBody Conference conference, @RequestBody Presentation presentation){
        Object conf = conferenceRepository.findById(id);
        Conference conferenceToAddPresentation;
        if (conf==null) {
            return ResponseEntity.status(404).body("There is not such conference");

        } else {
            conferenceToAddPresentation = conferenceRepository.findById(id).get();
            conferenceToAddPresentation.getPresentations().add(presentation);
            conferenceToAddPresentation.updateFrom(conference);
            conferenceRepository.save(conferenceToAddPresentation);
            return  ResponseEntity.ok(conferenceToAddPresentation);
        }
    }

    /*@PostMapping("/conference/{id}/sendNotification")
    ResponseEntity<?>sendNotification(@PathVariable long id,@PathVariable String owner, @RequestBody Conference conference, @RequestBody Notification notification){
        Object conf = conferenceRepository.findById(id);
        Conference conferenceToNotificate;
        if (conf==null) {
            return ResponseEntity.status(404).body("There is not such conference");

        } else {
            conferenceToNotificate = conferenceRepository.findById(id).get();
            conference.setNotifications(notification);

            return  ResponseEntity.ok(conferenceToNotificate);
        }
    }*/
    @PostMapping("/conference/{id}")
    ResponseEntity<?> editConference(@PathVariable long id, @RequestBody Conference updated) {
        var conferenceOpt = conferenceRepository.findById(id);
        Conference conferenceToUpdate;
        if (conferenceOpt.isEmpty()) {
            return ResponseEntity.status(404).body("There is no such conference");

        } else {
            conferenceToUpdate = conferenceRepository.findById(id).get();

            if (!requesterIsAuthor(conferenceToUpdate)) {
                return ResponseEntity.status(401).body("You must be the author to update a conference.");
            } else {
                conferenceToUpdate.updateFrom(updated);
                conferenceRepository.save(conferenceToUpdate);
                return ResponseEntity.ok(conferenceToUpdate);
            }
        }
    }

    private boolean requesterIsAuthor(Conference conf) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Author author = authorsRepository.findByUserId(user.getId());
        return author != null && author.getId().equals(conf.getAuthor().getId());
    }

    ConferenceController(ConferenceRepository conferenceRepository, AuthorRepository authorsRepository) {
        this.conferenceRepository = conferenceRepository;
        this.authorsRepository = authorsRepository;
    }
}
