package com.phildev.mls.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MlsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MlsGatewayApplication.class, args);
	}

}
