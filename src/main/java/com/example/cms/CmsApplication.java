package com.example.cms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner (ConferenceRepository conferenceRepository) {
		return args -> {
			conferenceRepository.save(new Conference("jak zyc", "rychu peja"));
			conferenceRepository.save(new Conference("interesting topic", "me"));
		};
	}
	
}


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

@Entity
class Conference {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String topic;
	
	@Column
	private String author;

	public Conference(String topic, String author) {
		this.topic = topic;
		this.author = author;
	}

	public Long getId() {
		return id;
	}

	public String getTopic() {
		return topic;
	}

	public String getAuthor() {
		return author;
	}

}

interface ConferenceRepository extends CrudRepository<Conference, Long> {}; 