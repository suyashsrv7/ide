package com.project.ide.dto;

import com.project.ide.model.User;

public class UserCodeDto {
	
	private String id;
	private String content;
	private String language;
	private Integer type;
	private Boolean sharable;
	
	public String getId() {
		return id;
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

	
}
