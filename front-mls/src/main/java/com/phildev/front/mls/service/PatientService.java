package com.phildev.front.mls.service;


import com.phildev.front.mls.error.PatientExistantException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PatientService {
    private final UserRepository userRepository;
    private final MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    public PatientService(UserRepository userRepository, MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy) {
        this.userRepository = userRepository;
        this.microserviceCoordonneesPatientProxy = microserviceCoordonneesPatientProxy;
    }

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
            log.error("Pas de patient présent en base de données pour la structure {}", user.getStructureId());
            return Collections.emptyList();
        }
    }

    public void supprimerPatient(Long id) {
        ResponseEntity<String> response = microserviceCoordonneesPatientProxy.supprimerPatient(id);
        if (response.getStatusCode().value()==200){
            log.info("Le patient avec l'id {} a bien été supprimé", id);
        }else{
            log.error("Le patient {} n'a pas été supprimé", id);
            throw new RuntimeException(String.format("Le patient %d n'a pas été supprimé", id));
        }
    }

    /**
     * Cette méthode sauvegarde un patient en base de données et vérifie au prélalble que le patient n'existe pas déjà
     * @param coordonneesPatient qui est l'objet à sauvergarder
     * @return les coordonnées d'un patient {@link CoordonneesPatient}
     */
    public CoordonneesPatient sauvegarderUnPatient(CoordonneesPatient coordonneesPatient){
        try{
            CoordonneesPatient patient  = microserviceCoordonneesPatientProxy.recuperePatientParNomPrenom(coordonneesPatient.getNom(), coordonneesPatient.getPrenom());
            log.error("Le patient {} {} existe déjà en base de données", patient.getPrenom(), patient.getNom());
            throw new PatientExistantException(String.format("Le patient %s %s existe déjà sur votre structure ou une autre.", patient.getPrenom(), patient.getNom()));

        }catch(ResponseNotFoundException responseNotFoundException){
            return microserviceCoordonneesPatientProxy.sauvegarderUnPatient(coordonneesPatient);
        }
    }

    /**
     * Cette méthode met à jour un patient en base de données et vérifie au préalable que le patient n'existe pas déjà sur une autre structure avec le nom/prénom mis à jour
     * @param coordonneesPatient qui est l'objet à mettre à jour
     * @return les coordonnées d'un patient {@link CoordonneesPatient}
     */
    public CoordonneesPatient miseAJourPatient(CoordonneesPatient coordonneesPatient){
        Long patientActuelId = coordonneesPatient.getId();
        CoordonneesPatient patientActuelEnBaseDeDonnees = microserviceCoordonneesPatientProxy.recuperePatient(patientActuelId);
        if(coordonneesPatient.getNom().equalsIgnoreCase(patientActuelEnBaseDeDonnees.getNom()) && coordonneesPatient.getPrenom().equalsIgnoreCase(patientActuelEnBaseDeDonnees.getPrenom())){
            log.info("Le patient est mis à jour avec le même nom {} prénom {}", coordonneesPatient.getNom(), coordonneesPatient.getPrenom());
            return microserviceCoordonneesPatientProxy.sauvegarderUnPatient(coordonneesPatient);
        }else {
            try {
                CoordonneesPatient patientAvecMemeNomPrenom = microserviceCoordonneesPatientProxy.recuperePatientParNomPrenom(coordonneesPatient.getNom(), coordonneesPatient.getPrenom());
                if (!Objects.equals(patientAvecMemeNomPrenom.getId(), patientActuelId)) {
                    log.error("Un autre patient existe avec le même nom en base données sur la structure {}", patientAvecMemeNomPrenom.getStructureId());
                    throw new PatientExistantException("Un autre patient existe avec le même nom en base données sur la structure " + patientAvecMemeNomPrenom.getStructureId());
                }
            }catch(ResponseNotFoundException responseNotFoundException){
                log.info("Pas de patient trouvé en base de données avec le prénom {} et le nom {}, le patient peut être mis à jour", coordonneesPatient.getPrenom(), coordonneesPatient.getNom());
                return microserviceCoordonneesPatientProxy.sauvegarderUnPatient(coordonneesPatient);
            }
        }
        return microserviceCoordonneesPatientProxy.sauvegarderUnPatient(coordonneesPatient);
    }


    public CoordonneesPatient recuperePatient(Long id) {
        return microserviceCoordonneesPatientProxy.recuperePatient(id);
    }


    public Page<CoordonneesPatient> recupereToutesLesCoordonneesPatientAvecPagination(Principal principal, int pageNo) {
        User user = userRepository.findByEmail(principal.getName());
        try {
            return microserviceCoordonneesPatientProxy.recupereCoordonneesParStructureAvecPagination(user.getStructureId(), pageNo);
        }catch (ResponseNotFoundException exception){
            log.info("L'erreur suivante est survenue pour la structure {} : {}", user.getStructureId(), exception.getMessage());
            throw new ResponseNotFoundException(exception.getMessage());
        }
    }
}
