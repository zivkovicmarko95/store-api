package com.store.storeauthapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StoreAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreAuthApiApplication.class, args);
	}

}
