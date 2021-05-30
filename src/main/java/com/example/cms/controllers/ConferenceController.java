package com.example.cms.controllers;

import com.example.cms.models.Conference;
import com.example.cms.repositories.ConferenceRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class ConferenceController {

	private final ConferenceRepository conferenceRepository;

	@GetMapping("/")
	String hello() {
		return "Hello there";
	}

	@GetMapping("/conferences")
	Iterable<Conference> conferences() {
		return conferenceRepository.findAll();
	}

	@PostMapping("/conference")
	ResponseEntity<?> createConference(@RequestBody Conference conferenceToAdd) {
		Conference result = conferenceRepository.save(conferenceToAdd);
		return ResponseEntity.ok().build();
	}

	ConferenceController(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}
}