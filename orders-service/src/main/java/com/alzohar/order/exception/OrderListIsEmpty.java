package com.alzohar.order.exception;

public class OrderListIsEmpty extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderListIsEmpty(String message) {
		super(message);
	}
}
