package com.project.ide.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.ide.config.JwtTokenUtil;
import com.project.ide.dto.TaskRequestDto;
import com.project.ide.dto.TaskResponseDto;
import com.project.ide.dto.TestResult;
import com.project.ide.service.CodeExecutionService;
import com.project.ide.service.CodeExecutionUtils;
import com.project.ide.service.FileHandlerService;

@RestController
@CrossOrigin
public class CodeExecutionController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private FileHandlerService fileHandler;

	@RequestMapping(value="/run", method=RequestMethod.POST)
	public ResponseEntity<?> run(@RequestBody TaskRequestDto task, @RequestHeader("Authorization") String bearerToken) throws IOException, InterruptedException {
//		/* Task response setup */
		TaskResponseDto taskResponse = new TaskResponseDto();
		taskResponse.setTotalInputs(task.getInputs().size());
		List<TestResult> resultList = new ArrayList<>();
		/* Setting up Utils */
		CodeExecutionUtils codeExecUtils = new CodeExecutionUtils();
		codeExecUtils.setLanguage(task.getLanguage());
		codeExecUtils.setDirname(resolveDirname(bearerToken));
//		
//		/* Setup srcFile */
		fileHandler.setFileContents(codeExecUtils.getDirname() + "srcFile" + codeExecUtils.getFileExtension(), task.getCode());
//		
//		/* Main Service */
		CodeExecutionService codeExecService = new CodeExecutionService();
//		
//		/* Evaluating each input */
		for(int i=0; i<task.getInputs().size(); i++) {
			fileHandler.setFileContents(codeExecUtils.getDirname() + "input.txt", task.getInputs().get(i));
			if(i != 0) codeExecUtils.getCompileCmds().clear();
			TestResult testResult = codeExecService.execute(codeExecUtils);
			resultList.add(testResult);
		}
		taskResponse.setTestResults(resultList);
		System.out.println("SDf");
		return new ResponseEntity(taskResponse, HttpStatus.OK);
		
	}
	
	private String resolveDirname(String bearerToken) {
		String token = bearerToken.split(" ")[1];
		return "tmpStore/" + jwtTokenUtil.getUsernameFromToken(token) + "/";
	}
}
