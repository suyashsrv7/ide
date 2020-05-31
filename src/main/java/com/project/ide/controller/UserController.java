package com.project.ide.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

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
import com.project.ide.dto.CodeDto;
import com.project.ide.dto.MessageDto;
import com.project.ide.dto.UserCodeDto;
import com.project.ide.dto.UserDefaultDto;
import com.project.ide.dto.UserDto;
import com.project.ide.model.User;
import com.project.ide.model.UserCode;
import com.project.ide.model.UserDefault;
import com.project.ide.service.MailClient;

@CrossOrigin
@RestController

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
	@Autowired
	private MailClient mailClient;

	private String uploadDir = "src/main/resources/static/uploads/";

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<?> hello() {
		return ResponseEntity.ok("Hello world");
	}


	@RequestMapping(value = "/get-user", method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String bearerToken) {
		System.out.println(bearerToken);
		User currUser = resolveCurrentUser(bearerToken);
		return new ResponseEntity<>(currUser, HttpStatus.OK);
	}

	@RequestMapping(value = "/save-code", method = RequestMethod.POST)
	public ResponseEntity<?> saveCode(@RequestHeader("Authorization") String bearerToken,
			@RequestBody UserCodeDto userCodeDto) {
		MessageDto newMsg = new MessageDto();
		User currUser = resolveCurrentUser(bearerToken);
		UserCode newUserCode = new UserCode();
		newUserCode.setId(userCodeDto.getId());
		newUserCode.setContent(userCodeDto.getContent());
		newUserCode.setTitle(userCodeDto.getTitle());
		newUserCode.setLanguage(userCodeDto.getLanguage());
		newUserCode.setType(userCodeDto.getType());
		newUserCode.setSharable(userCodeDto.getSharable());
		newUserCode.setUser(currUser);
		userCodeDao.save(newUserCode);
		newMsg.setErr(false);
		newMsg.setMsg("Code has been saved");
		return new ResponseEntity<>(newMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "/save-defaults", method = RequestMethod.POST)
	public ResponseEntity<?> saveDefaults(@RequestHeader("Authorization") String bearerToken,
			@RequestBody UserDefaultDto userDefaultDto) {
		User currUser = resolveCurrentUser(bearerToken);
		UserDefault newUserDefault = new UserDefault();
		if (userDefaultDto.getId() != null)
			newUserDefault.setId(userDefaultDto.getId());

		newUserDefault.setDefaultCode(userDefaultDto.getDefaultCode());
		newUserDefault.setFont(userDefaultDto.getFont());
		newUserDefault.setLanguage(userDefaultDto.getLanguage());
		newUserDefault.setTheme(userDefaultDto.getTheme());
		newUserDefault.setUser(currUser);
		userDefaultDao.save(newUserDefault);
		MessageDto newMsg = new MessageDto();
		newMsg.setErr(false);
		newMsg.setMsg("User defaults have been saved");
		return new ResponseEntity<>(newMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String bearerToken,
			@RequestBody UserDto userDto) {
		User currUser = resolveCurrentUser(bearerToken);
		System.out.println(userDto.getPassword());
		currUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
		userDao.save(currUser);
		MessageDto newMsg = new MessageDto();
		newMsg.setErr(false);
		newMsg.setMsg("Password changed successfully");
		return new ResponseEntity<>(newMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestHeader("Authorization") String bearerToken) throws IOException {
		String type = file.getContentType();
		long fileSize = file.getSize();
		if (fileSize > 3145728) {
			return new ResponseEntity<>("File size too large", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!"image".equals(type.split("/")[0])) {
			return new ResponseEntity<>("Invalid image", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		User currUser = resolveCurrentUser(bearerToken);
		String filename = currUser.getUsername() + "." + type.split("/")[1];
		File convertFile = new File(uploadDir + filename);
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		currUser.setImgUrl("uploads/" + filename);
		userDao.save(currUser);
		MessageDto newMsg = new MessageDto();
		newMsg.setErr(false);
		newMsg.setMsg("File is uploaded successfully: uploads/" + filename);
		return new ResponseEntity<>(newMsg, HttpStatus.OK);
	}

	@RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	public ResponseEntity<?> forgotPassword(@RequestParam(name = "email") String email) {
		User existingUser = userDao.findByEmail(email);
		MessageDto newMsg = new MessageDto();
		if (existingUser == null) {
			newMsg.setErr(true);
			newMsg.setMsg("Invalid email");
			return new ResponseEntity<>(newMsg, HttpStatus.UNAUTHORIZED);
		}
		String newPassword = generatePassword(7);
		existingUser.setPassword(bcryptEncoder.encode(newPassword));
		userDao.save(existingUser);
		mailClient.prepareAndSend(email, existingUser.getUsername(), newPassword);
		newMsg.setErr(false);
		newMsg.setMsg("Please check you email");
		return new ResponseEntity<>(newMsg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get-code", method = RequestMethod.GET)
	public ResponseEntity<?> getCode(@RequestParam(name = "codeId") String codeId) {
		System.out.println(codeId);
		Optional<UserCode> code = userCodeDao.findById(codeId);
		if(code.isEmpty() || !code.get().getSharable()) {
			MessageDto msgDto = new MessageDto();
			msgDto.setErr(true);
			msgDto.setMsg("Not found");
			return new ResponseEntity(msgDto, HttpStatus.NOT_FOUND);
		}
		UserCode userCode = code.get();
		CodeDto codeDto = new CodeDto();
		codeDto.setAuthor(userCode.getUser().getUsername());
		codeDto.setContent(userCode.getContent());
		codeDto.setLanguage(userCode.getLanguage());
		codeDto.setLink(userCode.getId());
		codeDto.setTitle(userCode.getTitle());

		return new ResponseEntity(codeDto, HttpStatus.OK);
	}

	private User resolveCurrentUser(String bearerToken) {
		String token = bearerToken.split(" ")[1];
		return userDao.findByUsername(jwtTokenUtil.getUsernameFromToken(token));
	}

	private String generatePassword(int n) {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}
}
