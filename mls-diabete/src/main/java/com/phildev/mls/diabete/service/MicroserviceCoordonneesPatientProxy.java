package com.phildev.mls.diabete.service;


import com.phildev.mls.diabete.model.CoordonneesPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-coordonnees-patient
 */
@FeignClient(name ="mls-gateway-coordonnees")
public interface MicroserviceCoordonneesPatientProxy {

    @GetMapping("/gateway/coordonneesPatient/{id}")
    CoordonneesPatient recuperePatient(@PathVariable("id") Long id);
}
