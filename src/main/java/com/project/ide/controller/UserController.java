package com.project.ide.controller;

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
import com.project.ide.dao.UserCodeDao;
import com.project.ide.dao.UserDao;
import com.project.ide.dto.UserCodeDto;
import com.project.ide.model.User;
import com.project.ide.model.UserCode;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserCodeDao userCodeDao;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
	@RequestMapping(value = "/get-user", method = RequestMethod.GET) 
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String bearerToken) {
		User currUser = resolveCurrentUser(bearerToken);
		return new ResponseEntity<>(currUser, HttpStatus.OK);
	}
	
	@RequestMapping(value = "save-code", method = RequestMethod.POST) 
	public ResponseEntity<?> saveCode(@RequestHeader("Authorization") String bearerToken, @RequestBody UserCodeDto userCodeDto) {
		User currUser = resolveCurrentUser(bearerToken);
		UserCode newUserCode = new UserCode();
		newUserCode.setId(userCodeDto.getId());
		newUserCode.setContent(userCodeDto.getContent());
		newUserCode.setLanguage(userCodeDto.getLanguage());
		newUserCode.setType(userCodeDto.getType());
		newUserCode.setSharable(userCodeDto.getSharable());
		newUserCode.setUser(currUser);
		userCodeDao.save(newUserCode);
		return new ResponseEntity<>("Code has been saved", HttpStatus.OK);
	}
	
	
	
	private User resolveCurrentUser(String bearerToken) {
		String token = bearerToken.split(" ")[1];
		return userDao.findByUsername(jwtTokenUtil.getUsernameFromToken(token));	
	}
}
	