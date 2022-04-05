package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;

public interface UserService {
	List<User> getAll(double min, double max, int offset, int limit, String sort);

	public void save(MultipartFile file) throws IOException;
}
