package com.alzohar.products.controller;

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

import com.alzohar.products.entity.Product;
import com.alzohar.products.exception.ProductListIsEmpty;
import com.alzohar.products.exception.ProductNotFound;
import com.alzohar.products.exporter.ProductExcelExporter;
import com.alzohar.products.exporter.ProductPDFExporter;
import com.alzohar.products.repository.ProductRepository;
import com.alzohar.products.service.ProductService;
import com.lowagie.text.DocumentException;

@RestController
public class ProductController {

	@Autowired
	ProductRepository proRepository;

	@Autowired
	ProductService proService;

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

//	getmapping for excel file download
	@GetMapping("/products/exp/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.ms-excel");
		DateFormat dateFormatter = new SimpleDateFormat("dd-mm-yyyy_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=products.xlsx";
		response.setHeader(headerKey, headerValue);

		List<Product> listProduct = proService.listAll();
		ProductExcelExporter excelExporter = new ProductExcelExporter(listProduct);
		excelExporter.export(response);
	}

//	Get Mapping for CSV file download
	@GetMapping("products/exp/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=products_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		List<Product> listProduct = proService.listAll();

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Product_Id", "Product_Name", "Product_Price", "Product_Brand", "Product_Description",
				"Product_Enabled", "Product_Created_At" };
		String[] nameMapping = { "id", "name", "price", "brand", "desc", "enabled", "createdAt" };

		csvWriter.writeHeader(csvHeader);

		for (Product product : listProduct) {
			csvWriter.write(product, nameMapping);
		}

		csvWriter.close();
	}

//	Get Mapping for PDF file download

	@GetMapping("/products/exp/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		Date date = new Date();

		String headerKey = "Content-Disposition";
		String headerValue = "inline; filename=products" + currentDateTime + ".pdf";
		response.setHeader("Content-Disposition", "attachment ;filename=products.pdf");

		List<Product> listProduct = proService.listAll();

		ProductPDFExporter exporter = new ProductPDFExporter(listProduct);
		exporter.export(response);
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
