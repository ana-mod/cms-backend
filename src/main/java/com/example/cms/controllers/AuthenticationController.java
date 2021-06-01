package com.example.cms.controllers;

import com.example.cms.forms.AuthenticationForm;
import com.example.cms.forms.RegisterForm;
import com.example.cms.jwt.JwtProvider;
import com.example.cms.models.User;
import com.example.cms.response.MessageResponse;
import com.example.cms.response.TokenResponse;
import com.example.cms.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    private final JwtProvider provider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService authenticationService,
                                    JwtProvider provider) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.provider = provider;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationForm authForm) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authForm.getLoginOrEmail(), authForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = provider.createToken(authentication);

        return ResponseEntity.ok(new TokenResponse(token));

    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterForm form) {
        if(authenticationService.registerUser(form)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User was registered"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Provided login or email is already in use."));
    }
}
