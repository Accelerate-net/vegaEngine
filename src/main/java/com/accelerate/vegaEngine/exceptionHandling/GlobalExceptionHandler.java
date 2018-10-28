package com.accelerate.vegaEngine.exceptionHandling;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler{

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleError(Exception e){
		
		ErrorResponse myResponse = new ErrorResponse();
		myResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		myResponse.setErrorMessage(e.getMessage());
		
		return new ResponseEntity<ErrorResponse> (myResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}