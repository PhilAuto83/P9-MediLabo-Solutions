package com.phildev.front.mls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.phildev.front.mls")
public class FrontMlsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontMlsApplication.class, args);
	}

}
