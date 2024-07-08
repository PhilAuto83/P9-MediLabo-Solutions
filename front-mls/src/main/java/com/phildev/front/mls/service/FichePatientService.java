package com.phildev.front.mls.service;

import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.repository.UserRepository;
import com.phildev.front.mls.utils.Utility;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FichePatientService {

    private static final Logger logger = LoggerFactory.getLogger(FichePatientService.class);

    @Autowired
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    public FichePatient recupereLaFichePatient(Long id){
        try{
            CoordonneesPatient coordonneesPatient = microserviceCoordonneesPatientProxy.recuperePatient(id);
            FichePatient fichePatient = new FichePatient();
            fichePatient.setId(coordonneesPatient.getId());
            fichePatient.setStructureId(coordonneesPatient.getStructureId());
            fichePatient.setNom(coordonneesPatient.getNom());
            fichePatient.setPrenom(coordonneesPatient.getPrenom());
            fichePatient.setAge(Utility.calculAgePatient(coordonneesPatient.getDateDeNaissance()));
            fichePatient.setAdresse(coordonneesPatient.getAdresse());
            fichePatient.setTelephone(coordonneesPatient.getTelephone());
            return fichePatient;
        }catch(FeignException exception){
            logger.error("Un problème est survenue {}", exception.getMessage());
            throw new ResponseNotFoundException(exception.getMessage());
        }


    }

}
