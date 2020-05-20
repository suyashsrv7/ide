package com.project.ide.dto;

public class TestResult {

	private String output;
	private Boolean error;
	private String errorContext;
	private String errorMsg;
	private Double execTime;

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Double getExecTime() {
		return execTime;
	}

	public void setExecTime(Double execTime2) {
		this.execTime = execTime2;
	}

	public String getErrorContext() {
		return errorContext;
	}

	public void setErrorContext(String errorContext) {
		this.errorContext = errorContext;
	}
	
	

}
