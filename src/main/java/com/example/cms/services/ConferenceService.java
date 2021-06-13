package com.example.cms.services;

import com.example.cms.exceptions.NoMatchingConferencesException;
import com.example.cms.exceptions.NoSuchConferenceException;
import com.example.cms.exceptions.UserUnauthorizedException;
import com.example.cms.models.Conference;
import com.example.cms.models.Presentation;
import com.example.cms.models.User;
import com.example.cms.repositories.ConferenceRepository;
import com.example.cms.repositories.PresentationRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final PresentationRepository presentationRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, PresentationRepository presentationRepository) {
        this.conferenceRepository = conferenceRepository;
        this.presentationRepository = presentationRepository;
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
        List<Conference> myConferences = conferenceRepository.findAllByCreatorId(user.getId());

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
        conferenceToCreate.setCreator(getCurrentlyLoggedUser());

        return conferenceRepository.save(conferenceToCreate);
    }

    @Transactional
    public Conference updateConference(long id, Conference updatedVersion) throws NoSuchConferenceException, UserUnauthorizedException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        if (!userIsAuthorized(conferenceOpt)) {
            throw new UserUnauthorizedException();
        }
        Conference toBeUpdated = conferenceOpt.get();
        toBeUpdated.updateFrom(updatedVersion);
        return conferenceRepository.save(toBeUpdated);
    }

    @Transactional
    public Conference addPresentationToExisting(long id, Presentation presentationToAdd) throws NoSuchConferenceException, UserUnauthorizedException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        if (!userIsAuthorized(conferenceOpt)) {
            throw new UserUnauthorizedException();
        }
        Conference toAddPresentationTo = conferenceOpt.get();
        toAddPresentationTo.addPresentationToExisting(presentationToAdd);
        presentationRepository.save(presentationToAdd);
        return conferenceRepository.save(toAddPresentationTo);
    }

    @Transactional
    public void deleteConference(long id) throws NoSuchConferenceException, UserUnauthorizedException {
        var conferenceOpt = conferenceRepository.findById(id);
        if (conferenceOpt.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        if (!userIsAuthorized(conferenceOpt)) {
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

    @Transactional
    public void disenrollFromEveryConference() {
        List<Long> conferencesId = getEnrolledConferencesId();
        for (Long id : conferencesId) {
            disenrollFromConference(id);
        }
    }

    public Set<User> getParticipants(long conferenceId) {
        Optional<Conference> conferenceOptional = conferenceRepository.findById(conferenceId);
        if (conferenceOptional.isEmpty()) {
            throw new NoSuchConferenceException();
        }
        return conferenceOptional.get().getUsers();
    }

    public List<Conference> getEnrolledConferences() {
        
        User user = getCurrentlyLoggedUser();

        List<Long> conferencesId = conferenceRepository.findEnrolledByUserId(user.getId());

        if(conferencesId == null) {
            throw new NoMatchingConferencesException();
        }

        List<Conference> enrolledConferences = new ArrayList<Conference>();
        for(Long id : conferencesId) {
            enrolledConferences.add(getConference(id));
        }

        return enrolledConferences;
    }

    private List<Long> getEnrolledConferencesId(){

        User user = getCurrentlyLoggedUser();

        List<Long> conferencesId = conferenceRepository.findEnrolledByUserId(user.getId());

        return conferencesId;
    }


    private boolean userIsAuthorized(Optional<Conference> conferenceOpt) {
        User currentlyLoggedUser = getCurrentlyLoggedUser();

        return conferenceOpt.map(
                conference -> conference.getCreator().getId()
                        .equals(
                                currentlyLoggedUser.getId()))
                .orElse(false);

    }

    private User getCurrentlyLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }


}
