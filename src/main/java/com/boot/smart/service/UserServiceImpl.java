package com.boot.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.smart.dao.UserRepository;
import com.boot.smart.entity.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User createUser(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public User getLoggedInUser(String email) {
		return this.userRepository.getUserByUsername(email);
	}

}
