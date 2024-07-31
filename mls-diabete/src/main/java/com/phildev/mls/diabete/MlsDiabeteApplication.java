package com.phildev.mls.diabete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.phildev.mls.diabete")
public class MlsDiabeteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MlsDiabeteApplication.class, args);
	}

}
