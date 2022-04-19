package com.alzohar.products.exception.handler;

import java.util.Date;

import lombok.Data;

@Data
public class ExceptionResponse {

	private String message;
	private String status;
	private Date timestamp;
	private String cause;

	public ExceptionResponse(String message, String status, Date timestamp, String cause) {
		super();
		this.message = message;
		this.status = status;
		this.timestamp = timestamp;
		this.cause = cause;
	}
}
