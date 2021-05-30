package com.example.cms.controllers;

import com.example.cms.models.Conference;
import com.example.cms.repositories.ConferenceRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

	ConferenceController(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}
}