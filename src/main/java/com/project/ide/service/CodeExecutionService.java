package com.project.ide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {

	@Autowired
	private FileHandlerService fileHandler;

	private String cwd;
	private String errContext;
	private String errMsg;

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getCwd() {
		return cwd;
	}

	public void setCwd(String cwd) {
		this.cwd = cwd;
	}
	
	public String getErrContext() {
		return errContext;
	}

	public void setErrContext(String errContext) {
		this.errContext = errContext;
	}

	public void handleErrors(int exitVal, String context) {
		this.setErrContext(context);
		if (exitVal == 255) {
			this.setErrMsg("Program Timed Out");
		} else if (exitVal == 136) {
			this.setErrMsg("Erroneous Arithmetic Operation");
		} else if (exitVal == 139) {
			this.setErrMsg("Segmentation Fault");
		} else if (exitVal == 134) {
			this.setErrMsg("Program Abort");
		}
	}

	

}
