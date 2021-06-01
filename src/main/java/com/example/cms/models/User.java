package com.example.cms.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "author", schema = "cms")
public class User implements Principal, UserDetails {
   
    @Id
	@GeneratedValue
	@Column(name = "UserId")
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

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new LinkedList<>();
		authorities.add(new SimpleGrantedAuthority("User"));
		return authorities;
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


	@Override
	public String getName() {
		return username;
	}
}
