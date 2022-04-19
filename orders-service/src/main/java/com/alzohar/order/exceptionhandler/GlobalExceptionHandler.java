package com.alzohar.order.exceptionhandler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alzohar.order.exception.OrderListIsEmpty;
import com.alzohar.order.exception.OrderNotFound;

@ControllerAdvice
public class GlobalExceptionHandler {

	ExceptionResponse response;

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<?> customerEmtyValueError(MethodArgumentNotValidException exception) {
		ExceptionResponse errorDetails = new ExceptionResponse("Validation Error", new Date(),
				HttpStatus.NOT_FOUND.name(), exception.getBindingResult().getFieldError().getDefaultMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OrderNotFound.class)
	public ResponseEntity<ExceptionResponse> orderNotFound(OrderNotFound exception) {
		response = new ExceptionResponse(exception.getMessage(), new Date(), HttpStatus.BAD_REQUEST.name(),
				exception.getClass().getSimpleName());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OrderListIsEmpty.class)
	public ResponseEntity<ExceptionResponse> orderListIsEmpty(OrderListIsEmpty exception) {
		response = new ExceptionResponse(exception.getMessage(), new Date(), HttpStatus.UNAUTHORIZED.name(),
				exception.getClass().getSimpleName());
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
	}

}
