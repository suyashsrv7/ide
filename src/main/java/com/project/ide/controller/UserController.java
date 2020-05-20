package com.project.ide.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.ide.config.JwtTokenUtil;
import com.project.ide.dao.UserCodeDao;
import com.project.ide.dao.UserDao;
import com.project.ide.dao.UserDefaultDao;
import com.project.ide.dto.UserCodeDto;
import com.project.ide.dto.UserDefaultDto;
import com.project.ide.dto.UserDto;
import com.project.ide.model.User;
import com.project.ide.model.UserCode;
import com.project.ide.model.UserDefault;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserCodeDao userCodeDao;
	@Autowired
	private UserDefaultDao userDefaultDao;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	private String uploadDir = "src/main/resources/static/uploads/";

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
	
	@RequestMapping(value = "save-defaults", method = RequestMethod.POST)
	public ResponseEntity<?> saveDefaults(@RequestHeader("Authorization") String bearerToken, @RequestBody UserDefaultDto userDefaultDto) {
		User currUser = resolveCurrentUser(bearerToken);
		UserDefault newUserDefault = new UserDefault();
		if(userDefaultDto.getId() != null) 
			newUserDefault.setId(userDefaultDto.getId());
		
		newUserDefault.setDefaultCode(userDefaultDto.getDefaultCode());
		newUserDefault.setFont(userDefaultDto.getFont());
		newUserDefault.setLanguage(userDefaultDto.getLanguage());
		newUserDefault.setTheme(userDefaultDto.getTheme());
		newUserDefault.setUser(currUser);
		userDefaultDao.save(newUserDefault);
		return new ResponseEntity<>("User defaults have been saved", HttpStatus.OK);
	}
	
	@RequestMapping(value = "change-password", method = RequestMethod.POST) 
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String bearerToken, @RequestBody UserDto userDto) {
		User currUser = resolveCurrentUser(bearerToken);
		currUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
		userDao.save(currUser);
		return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String bearerToken) throws IOException {
		String type = file.getContentType();
		long fileSize = file.getSize();
		if(fileSize > 3145728) {
			return new ResponseEntity<>("File size too large", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if(!"image".equals(type.split("/")[0])) {
			return new ResponseEntity<>("Invalid file type", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		User currUser = resolveCurrentUser(bearerToken);
		String filename = currUser.getUsername() + "." + type.split("/")[1];
		File convertFile = new File(uploadDir + filename);
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		currUser.setImgUrl("uploads/"+filename);
		userDao.save(currUser);
		return new ResponseEntity<>("File is uploaded successfully: uploads/" + filename , HttpStatus.OK);
	}
	
	private User resolveCurrentUser(String bearerToken) {
		String token = bearerToken.split(" ")[1];
		return userDao.findByUsername(jwtTokenUtil.getUsernameFromToken(token));	
	}
}
	