package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.User;

public interface UserRepository{

	List<User> getAllUsers(double min, double max, int offset, int limit, String sort);
	void saveUser(User user);
}
