package com.alzohar.service.controller;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.alzohar.service.entity.User;
import com.alzohar.service.entity.UserService;
import com.alzohar.service.exporter.UserExcelExporter;
import com.alzohar.service.exporter.UserPdfExporter;
import com.alzohar.service.repository.UserRepository;
import com.alzohar.service.setup.ExportService;
import com.alzohar.service.setup.SetUserService;
import com.lowagie.text.DocumentException;

@RestController
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService useService;

	@Autowired
	SetUserService service;

	@Autowired
	ExportService exportService;

	@GetMapping("/users/{id}")
	public Optional<User> getUserById(@PathVariable("id") long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isEmpty()) {
			LOGGER.info("User is not found with id " + id);
			throw new UsernameNotFoundException("User Not Found For Given id" + id);

		}
		LOGGER.info("User is found with id :: " + id);
		return user;

	}

	@GetMapping("/users")
	public List<User> getUsers() {
		List<User> list = userRepo.findAll();
		if (list.isEmpty()) {
			LOGGER.info("User is not found , user list is empty ");
			throw new UsernameNotFoundException("User Not Found ");
		}
		LOGGER.info("Get users list is successfull");
		return list;
	}

	@GetMapping("/export")
	public void export(HttpServletResponse response, @RequestParam("export") String export) {
		if (export.equalsIgnoreCase("excel")) {
			exportService.exportToExcel(response);
		} else if (export.equalsIgnoreCase("csv")) {
			exportService.exportToCSV(response);
		} else if (export.equalsIgnoreCase("pdf")) {
			exportService.exportToPDF(response);
		}
	}

	@PostMapping("/users")
	public User addUser(@RequestBody User user) {
		return userRepo.save(user);
	}

	@PutMapping("/users")
	public User updateUser(@RequestBody User user) {
		return userRepo.save(user);
	}

	@DeleteMapping("/users/{id}")
	public String deleteUserById(@PathVariable("id") long id) {
		userRepo.deleteById(id);
		return "User Delete Succesfully ";
	}
}
