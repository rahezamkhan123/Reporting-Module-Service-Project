package com.alzohar.interceptor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.alzohar.interceptor.entity.User;
import com.alzohar.interceptor.repository.UserRepository;
import com.alzohar.interceptor.service.MyUserDetails;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found !");
		}
		return new MyUserDetails(user);
	}
}
