package com.example.cms.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User {
   
    @Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	@Column
    private String password;
    
    @Column(unique = true)
	private String email;
	
	@ManyToMany(mappedBy = "users")
	private Set<Conference> conferences;

	public User(String username, String password, String email) {
		this.username = username;
        this.password = password;
        this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}