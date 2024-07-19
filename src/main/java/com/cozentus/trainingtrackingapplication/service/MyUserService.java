package com.cozentus.trainingtrackingapplication.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.repository.MyUserRepository;

@Service
public class MyUserService {

	private MyUserRepository myUserRepository;

	MyUserService(MyUserRepository myUserRepository) {

		this.myUserRepository = myUserRepository;
	}

	public MyUsers authenticate(String email, String password) {
		Optional<MyUsers> userCredentials = myUserRepository.findByUserEmail(email);
		if (userCredentials.isPresent() && userCredentials.get().getUserPassword().equals(password)) {
			return userCredentials.get();
		}
		return null;
	}

	public MyUsers addUser(MyUsers user) {
		return myUserRepository.save(user);
	}

	public MyUsers findById(Integer userId) {
		Optional<MyUsers> user = myUserRepository.findById(userId);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public MyUsers editUser(Integer userId, MyUsers userObj) {
		Optional<MyUsers> updatedUser = myUserRepository.findById(userId);
		if (updatedUser.isPresent()) {
//			get the user object
			MyUsers userToUpdate = updatedUser.get();

//			make changes in it
			userToUpdate.setUsername(userObj.getUsername());
			if (userObj.getUserEmail() != null) {
				userToUpdate.setUserPassword(userObj.getUserPassword());
				userToUpdate.setUserEmail(userObj.getUserEmail());
			}
			return myUserRepository.save(userToUpdate);
		}
		return null;
	}

	public MyUsers findByEmailId(String userEmail) {
		Optional<MyUsers> user = myUserRepository.findByUserEmail(userEmail);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public boolean deleteUser(Integer userId) {
		Optional<MyUsers> user = myUserRepository.findById(userId);
		if (user.isPresent()) {
			myUserRepository.delete(user.get());
			return true;
		}
		return false;
	}

	public boolean deleteByEmailId(String userEmail) {
		Optional<MyUsers> user = myUserRepository.findByUserEmail(userEmail);
		if (user.isPresent()) {
			myUserRepository.delete(user.get());
			return true;
		}
		return false;
	}
}