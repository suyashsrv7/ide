package com.project.ide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.ide.config.JwtTokenUtil;
import com.project.ide.dao.UserDao;
import com.project.ide.model.User;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
	@RequestMapping(value = "/get-user", method = RequestMethod.GET) 
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String bearerToken) {
		String token = bearerToken.split(" ")[1];
		User currUser = userDao.findByUsername(jwtTokenUtil.getUsernameFromToken(token));		
		return new ResponseEntity<>(currUser, HttpStatus.OK);
	}
}
	