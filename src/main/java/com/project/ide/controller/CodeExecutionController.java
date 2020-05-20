package com.project.ide.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.ide.dto.TaskRequestDto;

@RestController
@CrossOrigin
public class CodeExecutionController {

	@RequestMapping(value="/run", method=RequestMethod.POST)
	public ResponseEntity<?> run(@RequestBody TaskRequestDto task) {
		
		return null;
	}
}
