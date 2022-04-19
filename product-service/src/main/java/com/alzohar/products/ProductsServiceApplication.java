package com.alzohar.products;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient

@EntityScan(basePackages = { "com.alzohar.interceptor.entity", "com.alzohar.products.entity" })
@EnableJpaRepositories(basePackages = { "com.alzohar.interceptor.repository", "com.alzohar.products.repository" })
@SpringBootApplication(scanBasePackages = { "com.alzohar.products", "com.alzohar.interceptor" })
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

}
