package com.alzohar.products.controller;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.alzohar.products.entity.Product;
import com.alzohar.products.exception.ProductListIsEmpty;
import com.alzohar.products.exception.ProductNotFound;
import com.alzohar.products.exporter.ProductExcelExporter;
import com.alzohar.products.exporter.ProductPDFExporter;
import com.alzohar.products.repository.ProductRepository;
import com.alzohar.products.service.ExportService;
import com.alzohar.products.service.ProductService;
import com.itextpdf.text.DocumentException;

@RestController
public class ProductController {

	@Autowired
	ProductRepository proRepository;

	@Autowired
	ProductService proService;

	@Autowired
	ExportService expService;

	@GetMapping("/product/{id}")
	public Optional<Product> getOneProduct(@PathVariable(value = "id") long id) {
		Optional<Product> product = proRepository.findById(id);
		if (product != null) {
			return product;
		}
		throw new ProductNotFound("Product not found with given id = " + id);
	}

	@GetMapping("/product")
	public Optional<Product> getProductByName(@RequestParam("name") String name) {
		return proRepository.findByName(name);
	}

	@GetMapping("/products")
	List<Product> getProducts() {
		List<Product> products = proRepository.findAll();
		if (products.isEmpty()) {
			throw new ProductListIsEmpty("Product list is empty");
		}
		return products;
	}

	@GetMapping("/export")
	public void export(HttpServletResponse response, @RequestParam("export") String export) {
		if (export.equalsIgnoreCase("excel")) {
			expService.exportToExcel(response);
		} else if (export.equalsIgnoreCase("csv")) {
			expService.exportToCSV(response);
		} else if (export.equalsIgnoreCase("pdf")) {
			expService.exportToPDF(response);
		}
	}

	@PostMapping("/products")
	public Product addProduct(@RequestBody Product product) {
		return proRepository.save(product);
	}

	@PutMapping("/products")
	public Product updateProduct(@RequestBody Product product) {
		return proRepository.save(product);
	}

	@DeleteMapping("/products/{id}")
	public String deleteProduct(@PathVariable("id") long id) {
		proRepository.deleteById(id);
		return "Product Is Delete Successfully.";
	}
}
