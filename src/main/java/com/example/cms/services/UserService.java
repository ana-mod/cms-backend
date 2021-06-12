package com.example.cms.services;

import com.example.cms.models.User;
import com.example.cms.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

    public User getMyUser() {
        User user = getCurrentlyLoggedUser();
        return user;
    }

    private User getCurrentlyLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
    


}
