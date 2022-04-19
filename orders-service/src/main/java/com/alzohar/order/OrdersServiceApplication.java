package com.alzohar.order;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EntityScan(basePackages = { "com.alzohar.interceptor.entity", "com.alzohar.order.entity" })
@EnableJpaRepositories(basePackages = { "com.alzohar.interceptor.repository", "com.alzohar.order.repository" })
@SpringBootApplication(scanBasePackages = { "com.alzohar.order", "com.alzohar.interceptor" })
public class OrdersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersServiceApplication.class, args);
	}

}
