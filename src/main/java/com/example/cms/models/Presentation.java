package com.example.cms.models;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cms_presentation")
public class Presentation {
    @Id
	@GeneratedValue
    @Column(name = "PresentationId")
	private Long id;
    
    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthorId", referencedColumnName = "AuthorId")
    @JsonIgnore
    private Author author;

	@ManyToOne(targetEntity = Conference.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ConferenceId", referencedColumnName = "ConferenceId")
    @JsonIgnore
	private Conference conference;

	@Column(name = "Name")
    private String name;
    
    @Column(nullable = true, name = "FileName")
	private String fileName;

	@Column(name = "StartTime")
    private Date startTime;
    
    @Column(name = "EndTime")
    private Date endTime;
    
    @Column(name = "SubmitionDeadline")
	private Date submitionDeadline;

	public Presentation(String name, String fileName, Date startTime, Date endTime, Date submitionDeadline) {
		this.name = name;
        this.fileName = fileName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.submitionDeadline = submitionDeadline;
	}

    public Presentation() {

    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    private Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSubmitionDeadline() {
        return submitionDeadline;
    }

    public void setSubmitionDeadline(Date submitionDeadline) {
        this.submitionDeadline = submitionDeadline;
    }


}
