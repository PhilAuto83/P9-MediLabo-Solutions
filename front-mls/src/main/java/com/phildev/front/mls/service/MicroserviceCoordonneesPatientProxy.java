package com.phildev.front.mls.service;

import com.phildev.front.mls.model.CoordonneesPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-coordonnees-patient
 */
@FeignClient(name ="mls-gateway", url = "localhost:8081")
public interface MicroserviceCoordonneesPatientProxy {

    @GetMapping("/gateway/coordonneesPatient/structure/{id}")
    public List<CoordonneesPatient> recupereCoordonneesParStructure(@PathVariable("id") Integer id);
}
