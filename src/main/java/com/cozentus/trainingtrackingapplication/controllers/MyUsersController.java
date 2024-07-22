package com.cozentus.trainingtrackingapplication.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cozentus.trainingtrackingapplication.dto.CredentialsDTO;
import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.service.JwtService;
import com.cozentus.trainingtrackingapplication.service.MyUserService;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/users")
public class MyUsersController {
	private MyUserService credentialService;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;

	MyUsersController(MyUserService credentialService, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.credentialService = credentialService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
		Map<String, Object> response = new HashMap<>();

		try {
			// Attempt to authenticate the user
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentialsDTO.getEmail(), credentialsDTO.getPassword()));

			// If authentication is successful, proceed to generate the token
			if (authentication.isAuthenticated()) {
				UserDetails userDetails = credentialService.loadUserByUsername(credentialsDTO.getEmail());
				String token = jwtService.generateToken(userDetails);
				response.put("token", token);
				response.put("userRole", getUserRole(userDetails));
				response.put("username", userDetails.getUsername());
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Authentication failed"));
			}
		} catch (BadCredentialsException e) {
			// Handle the case where the password is incorrect
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("error", "Invalid  password"));
		} catch (UsernameNotFoundException e) {
			// Handle the case where the username is not found
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Invalid email address"));
		} catch (Exception e) {
			// Handle any other exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "An unexpected error occurred"));
		}
	}

	@PostMapping("/email")
	public ResponseEntity<Boolean> checkEmailAvailability(@RequestBody String email) {
		try {
			MyUsers user = credentialService.findByEmailId(email);
			if (user == null) {
				return ResponseEntity.ok(true);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	private String getUserRole(UserDetails userDetails) {
		return userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority)
				.orElse("ROLE_USER");
	}
}