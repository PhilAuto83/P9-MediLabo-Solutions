package com.phildev.mls.donnees.patient.service;

import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import com.phildev.mls.donnees.patient.repository.CoordonneesPatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoordonneesPatientService {

    private static final Logger logger = LoggerFactory.getLogger(CoordonneesPatientService.class);

    @Autowired
    private CoordonneesPatientRepository coordonneesPatientRepository;

    public CoordonneesPatient getAllCoordonneesPatientByNomEtPrenom(String nom, String prenom){
        return coordonneesPatientRepository.findByNomAndPrenom(nom, prenom);
    }

    public void deleteCoordonneesPatient(Long id) {
        coordonneesPatientRepository.deleteById(id);
        if(coordonneesPatientRepository.findById(id).isPresent()){
            logger.error("Une erreur est survenue, le patient n'a pas été supprimé");
            throw new RuntimeException(String.format("Le patient  avec l id %s n'a pas été supprimé", id));
        }else{
            logger.info("Le patient avec l id {} a été supprimé de la base de données", id);
        }
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
