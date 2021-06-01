package com.example.cms.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "conference", schema = "cms")
public
class Conference {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String topic;
	
	@Column
	private String name;

	@Column
	private Date startDate;
	
	@Column
	private Date endDate;

	@Column(length = 1000)
	private String description;

	@OneToMany(mappedBy = "conference")
	private List<Notification> notifications;

	@JoinTable
	@ManyToMany
	 private Set<User> users;

	@OneToMany(mappedBy = "conference")
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Presentation> presentations;

	public Conference() {}

	public Conference(String topic, String name, Date startDate, Date endDate, String description,
			List<Notification> notifications, Set<User> users, List<Presentation> presentations) {
		this.topic = topic;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.notifications = notifications;
		this.users = users;
		this.presentations = presentations;
	}

	public Conference(String topic, String name) {
		this.topic = topic;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public List<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(List<Presentation> presentations) {
		this.presentations = presentations;
	}




}
