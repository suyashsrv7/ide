package com.project.ide.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ide.dto.TestResult;

public class CodeExecutionService {

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
	
	public int executeCommand(List<String> cmds) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmds);
        File op = new File(getCwd() + "output.txt");
        File ip = new File(getCwd() + "input.txt");
        File er = new File(getCwd() + "error.txt");
        pb.directory(new File(getCwd()));
        pb.redirectError(er);
        pb.redirectInput(ip);
        pb.redirectOutput(op);
        Process p = pb.start();
        Boolean finishOnTime = p.waitFor(3000, TimeUnit.MILLISECONDS);
        int retVal;
        if (!finishOnTime) {
            p.destroy();
            retVal = 255;
        } else {
            retVal = p.exitValue();

        }
        return retVal;
    }
	
	public Boolean compile(List<String> cmds) throws IOException, InterruptedException {
        if(cmds.isEmpty()) return true;
        int retVal = executeCommand(cmds);
        System.out.println("Compilation: " + retVal);
        if(retVal != 0) {
            handleErrors(retVal, "Compilation error");
        }
        return (retVal == 0);
    }

    public Boolean run(List<String> cmds) throws IOException, InterruptedException {
        if(cmds.isEmpty()) return true;
        int retVal = executeCommand(cmds);
        System.out.println("Execution: " + retVal);
        if(retVal != 0) {
            handleErrors(retVal, "Runtime error");
        }
        return (retVal == 0);
    }
    
    public TestResult execute(CodeExecutionUtils execUtils) throws IOException, InterruptedException {
        setCwd(execUtils.getDirname());
        TestResult testResult = new TestResult();
        long start = 0, end = 0; double execTime = 0;
        if(compile(execUtils.getCompileCmds())) {
        	start = System.nanoTime();
        	run(execUtils.getRunCmds());
            end = System.nanoTime();
            execTime = (end - start) / 1000000000;
            System.out.println("Executed SuccessFully: " + (end - start)/1000000000);
        }
        testResult.setExecTime(execTime);
        if(getErrContext() != null) {
        	testResult.setError(true);
        	testResult.setErrorContext(getErrContext());
        	if(getErrMsg() != null) {
        		testResult.setErrorMsg(getErrMsg());
        	}
        	else {
        		testResult.setErrorMsg(getFileContents(execUtils.getDirname() + "error.txt"));
        	}
        }
        else {
        	testResult.setError(false);
        	String op = getFileContents(execUtils.getDirname() + "output.txt");
//        	String op = "";
        	testResult.setOutput(op);
        }
        return testResult;
    }
    
    public String getFileContents(String dirname) throws IOException {
		System.out.println(dirname);
		FileReader fr=new FileReader(dirname);    
        int i; String content = "";
        while((i=fr.read())!=-1)    
        	content += (char)i;
        fr.close(); 
        System.out.println("Content :" + content);
        return content;
	}

	

}
