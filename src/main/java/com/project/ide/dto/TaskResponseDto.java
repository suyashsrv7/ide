package com.project.ide.dto;

import java.util.List;

public class TaskResponseDto {
	private Integer totalInputs;
	private Integer totalEvaluated;
	private List<TestResult> testResults;
	public Integer getTotalInputs() {
		return totalInputs;
	}
	public void setTotalInputs(Integer totalInputs) {
		this.totalInputs = totalInputs;
	}
	public Integer getTotalEvaluated() {
		return totalEvaluated;
	}
	public void setTotalEvaluated(Integer totalEvaluated) {
		this.totalEvaluated = totalEvaluated;
	}
	public List<TestResult> getTestResults() {
		return testResults;
	}
	public void setTestResults(List<TestResult> testResults) {
		this.testResults = testResults;
	}
	

}
