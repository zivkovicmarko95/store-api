package com.store.storemanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreManagementApiApplication.class, args);
	}

}
