package com.example.demo.dto;

public class UploadResponse {

	int success;

	public UploadResponse(int success) {
		this.success = success;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
}
