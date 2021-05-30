package com.example.cms.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Notification {
    
    @Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn
	private Conference conference;
	
	@Column
	private String message;

	@Column
	private Date sentDate;

	public Notification(Conference conference, String message, Date sentDate) {
		this.conference = conference;
		this.message = message;
		this.sentDate = sentDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}


}
