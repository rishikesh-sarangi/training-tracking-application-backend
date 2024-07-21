package com.cozentus.trainingtrackingapplication.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.CredentialsDTO;
import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.service.MyUserService;
import com.cozentus.trainingtrackingapplication.util.ResponseUtil;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/users")
public class MyUsersController {
	private MyUserService credentialService;

	MyUsersController(MyUserService credentialService) {
		this.credentialService = credentialService;
	}

	@PostMapping("/login")
	public ResponseEntity<Object> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
		MyUsers user = credentialService.findByEmailId(credentialsDTO.getEmail());
		if (user != null) {
			if (user.getUserPassword().equals(credentialsDTO.getPassword())) {
				return ResponseUtil.generateResponse(HttpStatus.OK, List.of(user), "Login successful");
			} else {
				return ResponseUtil.buildErrorResponse("Wrong password", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return ResponseUtil.buildErrorResponse("Email doesn't exist", HttpStatus.NOT_FOUND);
		}
	}
}
