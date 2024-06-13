package com.phildev.mls.donnees.patient;

import com.phildev.mls.donnees.patient.repository.CoordonneesPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MlsDonneesPatientApplication implements CommandLineRunner {

	@Autowired
	private CoordonneesPatientRepository coordonneesPatientRepository;

	public static void main(String[] args) {
		SpringApplication.run(MlsDonneesPatientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		coordonneesPatientRepository.findAll().forEach(System.out::println);

	}
}
