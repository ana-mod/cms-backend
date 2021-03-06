package com.example.cms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "cms_user")
public class User implements Principal, UserDetails {

	@Id
	@GeneratedValue
	@Column(name = "UserId")
	private Long id;

	@Column(name = "Username", unique = true)
	private String username;

	@JsonIgnore
	@Column(name = "Password")
	private String password;

	@Column(name = "Email", unique = true)
	private String email;

	@JsonIgnore
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Conference> conferences;

	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private Author author;

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

	public Set<Conference> getConferences() {
		return conferences;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return id.equals(user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}