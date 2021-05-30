package com.example.cms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public
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