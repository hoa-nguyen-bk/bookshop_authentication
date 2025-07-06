package com.cybersoft.bookshop_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
public class BookshopAuthenticationApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookshopAuthenticationApplication.class, args);
	}

}
