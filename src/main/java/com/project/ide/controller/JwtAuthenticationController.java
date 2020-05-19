package com.project.ide.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.ide.config.JwtTokenUtil;
import com.project.ide.dao.UserDao;
import com.project.ide.dto.JwtRequest;
import com.project.ide.dto.JwtResponse;
import com.project.ide.dto.UserDto;
import com.project.ide.model.User;
import com.project.ide.service.JwtUserDetailsService;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	private UserDao userDao;
	

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		User sameUsername = userDao.findByUsername(user.getUsername());
		User sameEmail = userDao.findByEmail(user.getEmail());
		String responseMsg = "";
		if(sameUsername != null && sameEmail != null) {
			responseMsg = "User with same credentials already exists";
		}
		else if(sameUsername != null) {
			responseMsg = "User with same username already exists";
		}
		
		else if(sameEmail != null) {
			responseMsg = "User with same email already exists";
		}
		else {
			setupFiles(user.getUsername());
			userDetailsService.save(user);
			responseMsg = "Registered successfully";
		}
		
		return new ResponseEntity(responseMsg, HttpStatus.OK);
	}
	
	private void setupFiles(String dirname) throws IOException {
		Boolean dir = new File("tmpStore/" + dirname).mkdir();
		Boolean input = new File("tmpStore/" + dirname + "/input.txt").createNewFile();
		Boolean output = new File("tmpStore/" + dirname + "/output.txt").createNewFile();
		Boolean error = new File("tmpStore/" + dirname + "/error.txt").createNewFile();
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}