package com.project.ide.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserCode {

	@Id
	private String id;
	@Lob
	private String content;
	private String title;
	private String language;
	private Integer type;
	private Boolean sharable;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	public String getId() {
		return id;
	}
	
	

	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getSharable() {
		return sharable;
	}

	public void setSharable(Boolean sharable) {
		this.sharable = sharable;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "UserCode [id=" + id + ", content=" + content + ", language=" + language + ", type=" + type
				+ ", sharable=" + sharable + ", user=" + user + "]";
	}

}
