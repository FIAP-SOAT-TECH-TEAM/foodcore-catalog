package com.soat.fiap.food.core.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.soat.fiap.food.core.catalog", "com.soat.fiap.food.core.shared"})
@EnableScheduling @EnableDiscoveryClient
public class FoodCoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCoreApiApplication.class, args);
	}
}
