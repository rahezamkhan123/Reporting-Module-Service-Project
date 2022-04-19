package com.alzohar.products.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alzohar.products.entity.Product;
import com.alzohar.products.exception.ProductListIsEmpty;
import com.alzohar.products.exception.ProductNotFound;
import com.alzohar.products.repository.ProductRepository;

@RestController
public class ProductController {

	@Autowired
	ProductRepository proRepository;

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
