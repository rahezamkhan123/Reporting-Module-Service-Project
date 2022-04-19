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
import com.alzohar.order.service.OrderService;
import com.lowagie.text.DocumentException;

@RestController
public class OrderController {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;

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

//	Get Mapping for excel file download
	@GetMapping("/orders/exp/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.ms-excel");
		DateFormat dateFormatter = new SimpleDateFormat("dd-mm-yyyy_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=orders.xlsx";
		response.setHeader(headerKey, headerValue);

		List<Order> listOrder = orderService.listAll();
		OrderExcelExporter excelExporter = new OrderExcelExporter(listOrder);
		excelExporter.export(response);
	}

//	Get Mapping for CSV file download
	@GetMapping("orders/exp/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=orders_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		List<Order> listOrder = orderService.listAll();

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Order_Id", "Order_Name", "Order_Price", "Order_Quantity", "Order_Total",
				"Order_Order_Date" };
		String[] nameMapping = { "id", "name", "price", "quantity", "total", "orderDate" };

		csvWriter.writeHeader(csvHeader);

		for (Order order : listOrder) {
			csvWriter.write(order, nameMapping);
		}

		csvWriter.close();
	}

//	Get Mapping for PDF file download

	@GetMapping("/orders/exp/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		Date date = new Date();

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=orders" + currentDateTime + ".pdf";
		response.setHeader("Content-Disposition", "attachment ;filename=orders_" + date + ".pdf");

		List<Order> listOrder = orderService.listAll();

		OrderPDFExporter exporter = new OrderPDFExporter(listOrder);
		exporter.export(response);
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