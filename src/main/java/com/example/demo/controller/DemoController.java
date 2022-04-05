package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.Result;
import com.example.demo.dto.UploadResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping(value = "api/v1", produces = "application/json")
public class DemoController {

	@Autowired
	UserService userService;

	@GetMapping("/users")
	public ResponseEntity<Result> getResults(@RequestParam(name = "min", defaultValue = "0.0") double min,
			@RequestParam(name = "max", defaultValue = "4000.0") double max,
			@RequestParam(name = "offset", defaultValue = "0") int offset,
			@RequestParam(name = "limit", defaultValue = "0") int limit,
			@RequestParam(name = "sort", defaultValue = "") String sort) {
		List<User> userList = userService.getAll(min, max, offset, limit, sort);
		return ResponseEntity.status(HttpStatus.OK).body(new Result(userList));
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
		if (file != null && !file.getContentType().equals("csv")) {
			try {
				userService.save(file);
				return ResponseEntity.status(HttpStatus.OK).body(new UploadResponse(1));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadResponse(0));
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid File Type."));
		}
	}
}
