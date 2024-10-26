package com.boot.smart.service;


import com.boot.smart.entity.User;

public interface UserService {
	public User createUser(User user);

	public User getLoggedInUser(String email);
}
