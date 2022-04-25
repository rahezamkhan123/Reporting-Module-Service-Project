package com.alzohar.order.controller;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.alzohar.order.entity.Order;
import com.alzohar.order.exception.OrderListIsEmpty;
import com.alzohar.order.exception.OrderNotFound;
import com.alzohar.order.exporter.OrderExcelExporter;
import com.alzohar.order.exporter.OrderPDFExporter;
import com.alzohar.order.repository.OrderRepository;
import com.alzohar.order.service.ExportService;
import com.alzohar.order.service.OrderService;
import com.lowagie.text.DocumentException;

@RestController
public class OrderController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	ExportService expservice;

	@GetMapping("/orders")
	public List<Order> getAllOrders() {
		List<Order> list = orderRepository.findAll();
		if (list.isEmpty()) {
			throw new OrderListIsEmpty("Order list is Empty, First you have to add Order");
		}
		return list;
	}

	@GetMapping("/orders/{id}")
	public Optional<Order> getOrderById(@PathVariable("id") long id) {
		Optional<Order> order = orderRepository.findById(id);
		if (order.isEmpty()) {
			throw new OrderNotFound("Order Not Found with given id" + id);
		}
		return order;
	}

	@GetMapping("/export")
	public void export(HttpServletResponse response, @RequestParam("export") String export) {
		if (export.equalsIgnoreCase("excel")) {
			expservice.exportToExcel(response);
		} else if (export.equalsIgnoreCase("csv")) {
			expservice.exportToCSV(response);
		} else if (export.equalsIgnoreCase("pdf")) {
			expservice.exportToPDF(response);
		}
	}

	@PostMapping("/orders")
	public Order addOrder(@RequestBody Order order) {
		return orderRepository.save(order);
	}

	@PutMapping("/orders")
	public String updateOrder(@RequestBody Order order) {
		orderRepository.save(order);
		return "Order is Updated Successfully";
	}

	@DeleteMapping("/orders/{id}")
	public String deleteOrderById(@PathVariable("id") long id) {
		orderRepository.deleteById(id);
		return "Order Delete Succesfully ";
	}
}