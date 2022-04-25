package com.alzohar.service.setup;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alzohar.service.entity.User;
import com.alzohar.service.repository.UserRepository;

@Service
@Transactional
public class SetUserService {

	@Autowired
	UserRepository repository;

	public List<User> listAll() {
		return repository.findAll();
	}
}
