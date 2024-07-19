package com.cozentus.trainingtrackingapplication.controllers;

import java.util.Optional;

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

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/users")
public class MyUsersController {
	private MyUserService credentialService;

	MyUsersController(MyUserService credentialService) {
		this.credentialService = credentialService;
	}

	@PostMapping("/login")
	public ResponseEntity<MyUsers> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
		Optional<MyUsers> user = Optional
				.ofNullable(credentialService.authenticate(credentialsDTO.getEmail(), credentialsDTO.getPassword()));

		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
