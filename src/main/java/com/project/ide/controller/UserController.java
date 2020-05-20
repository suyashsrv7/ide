package com.project.ide.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok("Hello world");
	}
}
	