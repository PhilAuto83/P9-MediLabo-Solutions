package com.phildev.mls.donnees.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MlsCoordonneesPatientApplication  {


	public static void main(String[] args) {
		SpringApplication.run(MlsCoordonneesPatientApplication.class, args);
	}


}
