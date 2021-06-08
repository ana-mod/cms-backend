package com.example.cms.services;

import com.example.cms.exceptions.NoMatchingConferencesException;
import com.example.cms.exceptions.NoSuchConferenceException;
import com.example.cms.exceptions.UserUnauthorizedException;
import com.example.cms.models.Author;
import com.example.cms.models.Conference;
import com.example.cms.models.User;
import com.example.cms.repositories.AuthorRepository;
import com.example.cms.repositories.ConferenceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final AuthorRepository authorRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, AuthorRepository authorRepository) {
        this.conferenceRepository = conferenceRepository;
        this.authorRepository = authorRepository;
    }

    public Conference getConference(long id) throws NoSuchConferenceException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        return conferenceOpt.get();
    }

    public List<Conference> getMyConferences() throws NoMatchingConferencesException {
        User user = getCurrentlyLoggedUser();
        Author author = authorRepository.findByUserId(user.getId()).orElseThrow(NoMatchingConferencesException::new);
        List<Conference> myConferences = conferenceRepository.findAllByAuthorId(author.getId());

        if (myConferences.isEmpty()) {
            throw new NoMatchingConferencesException();
        }

        return myConferences;
    }

    public Iterable<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    @Transactional
    public Conference createConference(Conference conferenceToCreate) {
        Author author = findOrCreateAuthor();
        conferenceToCreate.setAuthor(author);

        return conferenceRepository.save(conferenceToCreate);
    }

    @Transactional
    public Conference updateConference(long id, Conference updatedVersion) throws NoSuchConferenceException, UserUnauthorizedException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        if (!userAuthorized(conferenceOpt)) {
            throw new UserUnauthorizedException();
        }
        Conference toBeUpdated = conferenceOpt.get();
        toBeUpdated.updateFrom(updatedVersion);
        return conferenceRepository.save(toBeUpdated);
    }

    @Transactional
    public void deleteConference(long id) throws NoSuchConferenceException, UserUnauthorizedException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        if (!userAuthorized(conferenceOpt)) {
            throw new UserUnauthorizedException();
        }
        conferenceRepository.delete(conferenceOpt.get());
    }

    @Transactional
    public void enrollToConference(long conferenceId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        if (conferenceOptional.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        User user = getCurrentlyLoggedUser();
        Conference conference = conferenceOptional.get();
        conference.getUsers().add(user);
        conferenceRepository.save(conference);
    }

    @Transactional
    public void disenrollFromConference(long conferenceId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        if (conferenceOptional.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        User user = getCurrentlyLoggedUser();
        Conference conference = conferenceOptional.get();
        conference.getUsers().remove(user);
        conferenceRepository.save(conference);
    }

    public Set<User> getParticipants(long conferenceId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        if (conferenceOptional.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        return conferenceOptional.get().getUsers();
    }


    private boolean userAuthorized(Optional<Conference> conferenceOpt) {
        User user = getCurrentlyLoggedUser();
        var optAuthor = authorRepository.findByUserId(user.getId());
        return optAuthor.map(
                author -> conferenceOpt.map(
                        conference -> conference.getAuthor().getId()
                                .equals(
                                        author.getId()))
                        .orElse(false))
                .orElse(false);
    }

    private Author findOrCreateAuthor() {
        User user = getCurrentlyLoggedUser();
        var optAuthor = authorRepository.findByUserId(user.getId());
        Author author;

        if (optAuthor.isPresent()) {
            author = optAuthor.get();
        } else {
            author = new Author(user);
            authorRepository.save(author);
        }

        return author;
    }

    private User getCurrentlyLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
