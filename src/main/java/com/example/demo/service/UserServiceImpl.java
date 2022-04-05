package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	public List<User> getAll(double min, double max, int offset, int limit, String sort) {
		return userRepo.getAllUsers(min, max, offset, limit, sort);
	}

	@Transactional
	public void save(MultipartFile file) throws IOException {
		List<String[]> userRecords = new ArrayList<>();
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));) {
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				userRecords.add(values);
			}
		}
		for (int i = 1; i < userRecords.size(); i++) {
			if (userRecords.get(i).length > 2) {
				throw new RuntimeException("Invalid Number of columns");
			}
				String name = userRecords.get(i)[0].trim();
				Double salary = Double.parseDouble(userRecords.get(i)[1]);
				if (salary > 0) {
					userRepo.saveUser(new User(name, salary));
				}
		}
	}
}
