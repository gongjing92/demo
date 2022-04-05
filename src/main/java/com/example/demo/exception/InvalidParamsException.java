package com.example.demo.exception;

public class InvalidParamsException extends RuntimeException {

	private static final long serialVersionUID = -9016460270323484203L;

	public InvalidParamsException(String errorMessage) {
		super(errorMessage);
	}
}
