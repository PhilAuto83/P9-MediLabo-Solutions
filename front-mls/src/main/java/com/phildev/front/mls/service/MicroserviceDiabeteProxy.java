package com.phildev.front.mls.service;


import com.phildev.front.mls.model.CoordonneesPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-diabete
 */
@FeignClient(name ="mls-gateway-diabete")
public interface MicroserviceDiabeteProxy {

    @GetMapping("/gateway/diabete/diagnostic/{patientId}")
    public String calculNiveauRisqueDiabete(@PathVariable("patientId") Integer id);

}
