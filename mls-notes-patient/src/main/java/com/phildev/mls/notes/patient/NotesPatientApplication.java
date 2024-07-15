package com.phildev.mls.notes.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotesPatientApplication {



	public static void main(String[] args) {
		SpringApplication.run(NotesPatientApplication.class, args);
	}



}
