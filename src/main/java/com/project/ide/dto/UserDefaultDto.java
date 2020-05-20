package com.project.ide.dto;

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ide.model.User;

public class UserDefaultDto {

	private Integer id;
	private String theme;
	private String font;
	private String language;
	private String defaultCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

}
