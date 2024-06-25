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
        return coordonneesPatientRepository.findByNomAndPrenom(nom, prenom);
    }

    public void deleteCoordonneesPatient(Long id) {
    }

    public Optional<CoordonneesPatient> findById(Long id) {
        return coordonneesPatientRepository.findById(id);
    }

    public CoordonneesPatient save(CoordonneesPatient coordonneesPatient) {
        return coordonneesPatientRepository.save(coordonneesPatient);
    }

    public Optional<CoordonneesPatient> getAllCoordonneesPatientById(Long id) {
        return coordonneesPatientRepository.findById(id);
    }

    public List<CoordonneesPatient> getAllCoordonneesPatientByStructureId(Integer id) {
        return coordonneesPatientRepository.findAllByStructureId(id);
    }
}