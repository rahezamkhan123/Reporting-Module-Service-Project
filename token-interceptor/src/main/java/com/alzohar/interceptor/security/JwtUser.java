package com.alzohar.interceptor.security;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtUser implements Serializable {

	private String username;
	private String password;
}
