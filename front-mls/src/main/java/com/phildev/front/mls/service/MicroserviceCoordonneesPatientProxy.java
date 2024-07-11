package com.phildev.front.mls.service;


import com.phildev.front.mls.model.CoordonneesPatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-coordonnees-patient
 */
@FeignClient(name ="mls-gateway")
public interface MicroserviceCoordonneesPatientProxy {

    @GetMapping("/gateway/coordonneesPatient/structure/{id}")
    public List<CoordonneesPatient> recupereCoordonneesParStructure(@PathVariable("id") Integer id);

    @GetMapping("/gateway/coordonneesPatient")
    public CoordonneesPatient recupereCoordonneesParNomPrenom(@RequestParam("nom") String nom, @RequestParam("prenom") String prenom);


    @DeleteMapping("/gateway/coordonneesPatient/delete/{id}")
    ResponseEntity<String> supprimerPatient(@PathVariable("id") Long id);

    @PostMapping("/gateway/coordonneesPatient")
    CoordonneesPatient sauvegarderUnPatient(@RequestBody CoordonneesPatient coordonneesPatient);

    @GetMapping("/gateway/coordonneesPatient/{id}")
    CoordonneesPatient recuperePatient(@PathVariable("id") Long id);

    @GetMapping("/gateway/coordonneesPatient/structure/{id}/page")
    Page<CoordonneesPatient> recupereCoordonneesParStructureAvecPagination(@PathVariable("id") Integer id, @RequestParam("pageNo") int pageNo);

}
