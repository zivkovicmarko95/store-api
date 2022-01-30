package com.store.storeorderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreOrderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreOrderApiApplication.class, args);
	}

}
