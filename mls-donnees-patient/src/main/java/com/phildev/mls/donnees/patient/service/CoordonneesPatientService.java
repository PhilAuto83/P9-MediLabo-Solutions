package com.phildev.mls.donnees.patient.service;

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




    public CoordonneesPatient getAllCoordonneesPatientByNomEtPrenom(String nom, String prenom){
        return Optional.of(coordonneesPatientRepository.findByNomAndPrenom(nom, prenom)).orElse(null);
    }

    public void deleteCoordonneesPatient(String id) {
    }

    public Optional<CoordonneesPatient> findById(String id) {
        return coordonneesPatientRepository.findById(id);
    }

    public CoordonneesPatient save(CoordonneesPatient coordonneesPatient) {
        return coordonneesPatientRepository.save(coordonneesPatient);
    }

    public Optional<CoordonneesPatient> getAllCoordonneesPatientById(String id) {
        return coordonneesPatientRepository.findById(id);
    }

    public List<CoordonneesPatient> getAllCoordonneesPatientByStructureId(Integer id) {
        return coordonneesPatientRepository.findAllByStructureId(id);
    }
}
