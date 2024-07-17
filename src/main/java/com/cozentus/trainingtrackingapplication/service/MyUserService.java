package com.cozentus.trainingtrackingapplication.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.repository.MyUserRepository;

@Service
public class MyUserService {
		
	@Autowired
	private MyUserRepository credentialRepository;
	
	public MyUsers authenticate(String email, String password) {
		Optional<MyUsers> userCredentials = credentialRepository.findByUserEmail(email);
		if(userCredentials.isPresent() 
				&& userCredentials.get().getUserPassword().equals(password)) {
			return userCredentials.get();
		}
		return null;
	}	
}
