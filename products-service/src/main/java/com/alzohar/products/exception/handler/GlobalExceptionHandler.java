package com.alzohar.products.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alzohar.products.exception.ProductAlreadyExist;
import com.alzohar.products.exception.ProductListIsEmpty;
import com.alzohar.products.exception.ProductNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {

	ExceptionResponse response;

	@ExceptionHandler(value = ProductNotFound.class)
	public ResponseEntity<ExceptionResponse> productNotFoundException(ProductNotFound exception) {
		response = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND.name(), new Date(),
				exception.getClass().getSimpleName());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = ProductAlreadyExist.class)
	public ResponseEntity<ExceptionResponse> productAlreadyExistException(ProductAlreadyExist exception) {
		response = new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.name(), new Date(),
				exception.getClass().getSimpleName());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ProductListIsEmpty.class)
	public ResponseEntity<ExceptionResponse> productListIsEmptyException(ProductListIsEmpty exception) {
		response = new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.name(), new Date(),
				exception.getClass().getSimpleName());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
