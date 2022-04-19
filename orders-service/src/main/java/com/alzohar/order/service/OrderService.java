package com.alzohar.order.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alzohar.order.entity.Order;
import com.alzohar.order.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	@Autowired
	OrderRepository repository;

	public List<Order> listAll() {
		return repository.findAll();
	}
}
