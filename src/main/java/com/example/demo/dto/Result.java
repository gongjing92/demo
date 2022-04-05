package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
	
	public Result() {
	}

	public Result(List<User> users) {
		this.users = users;
	}

	@JsonProperty("results")
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
