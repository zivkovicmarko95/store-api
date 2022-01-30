package com.store.storeanalyticsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreAnalyticsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreAnalyticsApiApplication.class, args);
	}

}
