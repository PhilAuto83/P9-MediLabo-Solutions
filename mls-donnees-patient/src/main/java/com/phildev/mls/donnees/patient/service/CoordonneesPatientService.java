package com.phildev.mls.donnees.patient.service;

import com.netflix.discovery.converters.Auto;
import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import com.phildev.mls.donnees.patient.repository.CoordonneesPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoordonneesPatientService {

    @Autowired
    private CoordonneesPatientRepository coordonneesPatientRepository;


    public List<CoordonneesPatient> getAllCoordonneesPatient(){
        return Optional.of(coordonneesPatientRepository.findAll()).orElse(new ArrayList<>());

    }
}
