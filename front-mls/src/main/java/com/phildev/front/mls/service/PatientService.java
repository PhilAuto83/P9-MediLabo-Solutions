package com.phildev.front.mls.service;


import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    /**
     * Cette méthode permet de récupérer tous les coordonnées des patients associés à la structure de l'utilisateur connecté
     * @param principal qui est l'objet permettant de récupérer le user connecté et son structure id
     * @return une liste de {@link CoordonneesPatient} qui représente les coordonnées d'un patient (téléphone, adresse, nom, prénom)
     */
    public List<CoordonneesPatient> recupereToutesLesCoordonneesPatient(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        try {
            return microserviceCoordonneesPatientProxy.recupereCoordonneesParStructure(user.getStructureId());
        }catch (FeignException exception){
            logger.info("Pas de patient présent en base de données pour la structure {}", user.getStructureId());
            return Collections.emptyList();
        }
    }

    public void supprimerPatient(Long id) {
        ResponseEntity<String> response = microserviceCoordonneesPatientProxy.supprimerPatient(id);
        if (response.getStatusCode().value()==200){
            logger.info("Le patient avec l'id {} a bien été supprimé", id);
        }else{
            logger.error("Le patient {} n'a pas été supprimé", id);
            throw new RuntimeException(String .format("Le patient %d n'a pas été supprimé", id));
        }
    }

    public CoordonneesPatient ajouterUnPatient(CoordonneesPatient coordonneesPatient){
        return microserviceCoordonneesPatientProxy.ajoutPatient(coordonneesPatient);
    }
}
