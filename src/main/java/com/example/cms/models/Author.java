package com.example.cms.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Author {
    
    @Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "FirstName")
	private String firstName;
	
	@Column(name = "LastName")
    private String lastName;
    
    @Column(name = "Birthday")
    private Date birthday;

    @Column(name="BioDetails", length = 1000, nullable = true)
    private String bioDetails;

    @OneToOne
    @JoinColumn(name = "UserId")
    private User user;

    @OneToMany(mappedBy = "conference")
	private List<Presentation> presentations;

    public Author(String firstName, String lastName, Date birthday, String bioDetails, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.bioDetails = bioDetails;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBioDetails() {
        return bioDetails;
    }

    public void setBioDetails(String bioDetails) {
        this.bioDetails = bioDetails;
    }

}