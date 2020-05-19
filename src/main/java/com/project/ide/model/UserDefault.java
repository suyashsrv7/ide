package com.project.ide.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserDefault {

	@Id
	private Integer id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	private String theme;
	private String font;
	private String language;
	@Lob
	private String defaultCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefaultCode() {
		return defaultCode;
	}

	public void setDefaultCode(String defaultCode) {
		this.defaultCode = defaultCode;
	}

	@Override
	public String toString() {
		return "UserDefault [id=" + id + ", user=" + user + ", theme=" + theme + ", font=" + font + ", language="
				+ language + ", defaultCode=" + defaultCode + "]";
	}

}
