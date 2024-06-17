package com.phildev.mls.donnees.patient.controller;

import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import com.phildev.mls.donnees.patient.service.CoordonneesPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoordonneesPatientController {

    @Autowired
    private CoordonneesPatientService coordonneesPatientService;
    @GetMapping("/coordonneesPatient/all")
    public List<CoordonneesPatient> getAllCoordonneesPatient(){
        return coordonneesPatientService.getAllCoordonneesPatient();
    }
}
