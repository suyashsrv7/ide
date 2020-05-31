package com.project.ide.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class CodeExecutionUtils {
	
	private List<String> compileCmds;
	private List<String> runCmds;
	private String language;
	private String fileExtension;
	private String dirname;
	
	@Autowired
	private FileHandlerService fileHandler;

	public List<String> getCompileCmds() {
		return compileCmds;
	}

	public void setCompileCmds(List<String> compileCmds) {
		this.compileCmds = compileCmds;
	}

	public List<String> getRunCmds() {
		return runCmds;
	}

	public void setRunCmds(List<String> runCmds) {
		this.runCmds = runCmds;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
		this.generate();
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	

	public String getDirname() {
		return dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
	}

	private void generate() {
		List<String> cmd1 = new ArrayList<>();
		List<String> cmd2 = new ArrayList<>();

		if (language.equals("C++")) {
			cmd1.add("g++");
			cmd1.add("-o");
			cmd1.add("srcFile.out");
			cmd1.add("srcFile.cpp");
			cmd2.add("./srcFile.out");
			setFileExtension(".cpp");
		} else if (language.equals("C")) {
			cmd1.add("gcc");
			cmd1.add("-o");
			cmd1.add("srcFile.out");
			cmd1.add("srcFile.c");
			cmd2.add("./srcFile.out");
			setFileExtension(".c");
		} else if (language.equals("Java")) {
			cmd1.add("javac");
			cmd1.add("srcFile.java");
			cmd2.add("java");
			cmd2.add("srcFile");
			setFileExtension(".java");
		} else if (language.equals("Python 2.7")) {
			cmd2.add("python");
			cmd2.add("srcFile.py");
			setFileExtension(".py");
		} else if (language.equals("Python 3.6")) {
			cmd2.add("python3");
			cmd2.add("srcFile.py");
			setFileExtension(".py");
		}

		setCompileCmds(cmd1);
		setRunCmds(cmd2);
	}
}
