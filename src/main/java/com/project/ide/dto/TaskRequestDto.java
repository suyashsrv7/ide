package com.project.ide.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TaskRequestDto implements Serializable{
	private String language;
	private String code;
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<String> inputs;
	private long timeLimit;
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getInputs() {
		return inputs;
	}
	public void setInputs(List<String> inputs) {
		this.inputs = inputs;
	}
	public long getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	@Override
	public String toString() {
		return "TaskRequestDto [language=" + language + ", code=" + code + ", inputs=" + inputs + ", timeLimit="
				+ timeLimit + "]";
	}
	
	
	
	
}
