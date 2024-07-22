package com.cozentus.trainingtrackingapplication.service;

import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cozentus.trainingtrackingapplication.model.MyUsers;
import com.cozentus.trainingtrackingapplication.repository.MyUserRepository;

@Service
public class MyUserService implements UserDetailsService {

	private MyUserRepository myUserRepository;

	MyUserService(MyUserRepository myUserRepository) {

		this.myUserRepository = myUserRepository;
	}

	public MyUsers authenticate(String email, String password) throws AuthenticationException {
		Optional<MyUsers> userOptional = myUserRepository.findByUserEmail(email);

		if (userOptional.isPresent()) {
			MyUsers user = userOptional.get();
			if (user.getUserPassword().equals(password)) {
				return user;
			} else {
				throw new AuthenticationException("Wrong password");
			}
		} else {
			throw new AuthenticationException("Email doesn't exist");
		}
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, BadCredentialsException {
	    Optional<MyUsers> user = myUserRepository.findByUserEmail(username);
	    if (user.isPresent()) {
	        var userObj = user.get();
	        return User.builder()
	                .username(userObj.getUserEmail())
	                .password(userObj.getUserPassword())
	                .roles(userObj.getUserRole().toUpperCase())
	                .build();
	    } else {
	        throw new UsernameNotFoundException(username);
	    }
	}
}