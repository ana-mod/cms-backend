package com.example.cms.models;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "author", schema = "cms")
public class Presentation {
    @Id
	@GeneratedValue
    @Column(name = "PresentationId")
	private Long id;
    
    @ManyToOne
    @JoinColumn(name = "AuthorId")
    private Author author;

	@ManyToOne
	@JoinColumn(name = "ConferenceId")
	private Conference conference;

	@Column(name = "Name")
    private String name;
    
    @Column(nullable = true, name = "FileName")
	private String fileName;

	@Column(name = "StartTime")
    private Date startTime;
    
    @Column(name = "EndTime")
    private Date endTime;
    
    @Column(nullable = true, name = "SubmitionDeadline")
	private Date submitionDeadline;

	public Presentation(String name, String fileName, Date startTime, Date endTime, Date submitionDeadline) {
		this.name = name;
        this.fileName = fileName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.submitionDeadline = submitionDeadline;
	}

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Conference getConference() {
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
