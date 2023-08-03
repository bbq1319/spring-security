package com.luxurystar.springsecurity.common;

import java.rmi.AccessException;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(AccessException.class)
	public ResponseEntity<AccessException> accessExceptionHandler(AccessException e) {
		return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<AuthenticationException> authenticationExceptionHandler(AuthenticationException e) {
		return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> exceptionHandler(Exception e) {
		return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	}

}
