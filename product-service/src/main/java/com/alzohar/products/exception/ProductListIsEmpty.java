package com.alzohar.products.exception;

public class ProductListIsEmpty extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductListIsEmpty(String message) {
		super(message);
	}
}
