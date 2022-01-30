package com.store.storenotificationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreNotificationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreNotificationApiApplication.class, args);
	}

}
