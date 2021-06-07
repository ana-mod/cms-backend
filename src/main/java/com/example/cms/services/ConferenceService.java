package com.example.cms.services;

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

import java.util.Optional;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final AuthorRepository authorRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, AuthorRepository authorRepository) {
        this.conferenceRepository = conferenceRepository;
        this.authorRepository = authorRepository;
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

    public Conference getConference(long id) throws NoSuchConferenceException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        return conferenceOpt.get();
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
