package com.phildev.front.mls.service;

import com.phildev.front.mls.model.CoordonneesPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-coordonnees-patient
 */
@FeignClient(name ="mls-gateway")
public interface MicroserviceCoordonneesPatientProxy {

    @GetMapping("/gateway/coordonneesPatient/structure/{id}")
    public List<CoordonneesPatient> recupereCoordonneesParStructure(@PathVariable("id") Integer id);


    @DeleteMapping("/gateway/coordonneesPatient/delete/{id}")
    ResponseEntity<String> supprimerPatient(@PathVariable("id") Long id);
}
