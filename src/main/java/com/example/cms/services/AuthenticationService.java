package com.example.cms.services;

import com.example.cms.forms.RegisterForm;
import com.example.cms.models.User;
import com.example.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {

        User user = userRepository.authenticateByLoginOrEmail(loginOrEmail);

        if(user == null){
            throw new UsernameNotFoundException(loginOrEmail);
        }
        return user;
    }

    public boolean registerUser(RegisterForm form) {

        User existingUser = userRepository.getByUsernameOrEmail(form.getUsername(), form.getEmail());
        if(existingUser != null) {
            return false;
        }

        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(encoder.encode(form.getPassword()));

        userRepository.save(user);
        return true;

    }
}
