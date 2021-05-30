package com.example.cms;

import com.example.cms.models.Conference;
import com.example.cms.repositories.ConferenceRepository;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

	// @Bean
	// ApplicationRunner applicationRunner (ConferenceRepository conferenceRepository) {
	// 	return args -> {
	// 		conferenceRepository.save(new Conference("jak zyc", "rychu peja"));
	// 		conferenceRepository.save(new Conference("interesting topic", "me"));
	// 	};
	// }
	
}; 