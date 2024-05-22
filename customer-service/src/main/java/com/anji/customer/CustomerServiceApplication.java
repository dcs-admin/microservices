package com.anji.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class CustomerServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	@Qualifier("restTemplate")
	public RestTemplate loadRestTemplate(){
		return new RestTemplate();
	}

	@Bean
	@Qualifier("externalRestTemplate")
	public RestTemplate loadExternalRestTemplate(){
		return new RestTemplate();
	}

}
