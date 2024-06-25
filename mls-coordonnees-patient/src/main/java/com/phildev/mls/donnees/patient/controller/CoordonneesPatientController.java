package com.phildev.mls.donnees.patient.controller;

import com.phildev.mls.donnees.patient.exception.PatientException;
import com.phildev.mls.donnees.patient.exception.PatientNonTrouveException;
import com.phildev.mls.donnees.patient.exception.StructureNonTrouveeException;
import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import com.phildev.mls.donnees.patient.service.CoordonneesPatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class CoordonneesPatientController {
    private static final  Logger LOGGER = LoggerFactory.getLogger(CoordonneesPatientController.class);

    @Autowired
    private CoordonneesPatientService coordonneesPatientService;

    /**
     * Cette méthode retourne la liste des coordonnées de tous les patients par structure avec leur nom, prénom, adresse, n° de téléphone
     * @return une liste de {@link CoordonneesPatient}
     */
    @GetMapping("/coordonneesPatient/structure/{id}")
    public List<CoordonneesPatient> getAllCoordonneesPatientByStructureId(@PathVariable("id")@NotNull(message = "l'id ne doit pas être vide") Integer id){
        List<CoordonneesPatient> coordonneesPatients = coordonneesPatientService.getAllCoordonneesPatientByStructureId(id);
        if(coordonneesPatients.isEmpty()){
             LOGGER.error("Pas de structure trouvée avec l'id {}", id);
             throw new StructureNonTrouveeException("Pas de structure trouvée avec l'id "+id);
         }
        return coordonneesPatients;
    }



    /**
     * Cette méthode retourne les coordonnées d'un patient retoruvé via son id sur la base mongo
     * @param id qui est l'id du patient sous forme de chaîne de caractères
     * @return les coordonnées d'un patient -> {@link CoordonneesPatient}.
     * Si le patient n'est pas trouvé une exception est retourné de type {@link PatientNonTrouveException}
     */
    @GetMapping("/coordonneesPatient/{id}")
    public Optional<CoordonneesPatient> getCoordonneesPatient(@PathVariable("id") @NotNull(message = "l 'id ne doit pas être vide ou nul") Long id ){
        Optional<CoordonneesPatient> coordonneesPatient =  coordonneesPatientService.getAllCoordonneesPatientById(id);
        if(coordonneesPatient.isPresent()) {
            return coordonneesPatientService.getAllCoordonneesPatientById(id);
        }
        LOGGER.error("Le patient avec l'id {}} n'a pas été trouvé",id);
        throw new PatientNonTrouveException(String.format("Le patient avec l'id %s n'a pas été trouvé", id));
    }

    /**
     * Cette méthode retourne les coordonnées d'un patient en passant en paramètre son nom et son prénom.
     * @param nom
     * @param prenom
     * @return les coordonnées du patient {@link CoordonneesPatient}.
     * On renvoie une exception si le patient n'est pas trouvé  de type {@link PatientNonTrouveException}
     */
    @GetMapping("/coordonneesPatient")
    public CoordonneesPatient getCoordonneesPatient(@RequestParam("nom")  @NotBlank(message = "le nom ne doit pas être nul ou vide") String nom,
                                                    @RequestParam("prenom") @NotBlank(message = "le prénom ne doit pas être nul ou vide") String prenom){
        String nomEnMajuscules = nom.toUpperCase();
        String prenomEnMajuscules = prenom.toUpperCase();
        CoordonneesPatient coordonneesPatient = coordonneesPatientService.getAllCoordonneesPatientByNomEtPrenom(nomEnMajuscules, prenomEnMajuscules);
        if(coordonneesPatient == null){
            LOGGER.error("Aucun patient trouvé avec le nom {} et prénom {}", nom, prenom);
            throw new PatientNonTrouveException(String.format("Aucun patient trouvé avec le nom %s et prénom %s", nom, prenom));
        }
        return  coordonneesPatient;
    }

    /**
     * Cette méthode retourne un code 201 si les coordonnées du patient sont sauvegardés en base de données.
     * La méthode prend un objet de type {@link CoordonneesPatient} dans le body
     * @param coordonneesPatient
     * @return les coordonnées du patient.     *
     * La méthode peut retourner une exception si le patient créé est nul -> {@link PatientException}
     */
    @PostMapping(value = "/coordonneesPatient", consumes="application/json", produces = "application/json")
    public ResponseEntity<CoordonneesPatient> createPatient(@Valid @RequestBody CoordonneesPatient coordonneesPatient){
        coordonneesPatient.setNom(coordonneesPatient.getNom().toUpperCase());
        coordonneesPatient.setPrenom(coordonneesPatient.getPrenom().toUpperCase());
        try{
            CoordonneesPatient patientCree = coordonneesPatientService.save(coordonneesPatient);
            URI currentUri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .buildAndExpand()
                    .toUri();
            return ResponseEntity.created(currentUri).body(patientCree);
        }catch(Exception exception){
            LOGGER.error("Une erreur est survenue, le patient n'a pas été créé");
            throw new PatientException("Une erreur est survenue, le patient n'a pas été créé");
        }
    }

    /**
     * Cette méthode supprime les coordonnnées d'un patient par son id
     * @param id
     * @return un message de suppression du patient
     */
    @DeleteMapping(value = "/coordonneesPatient/delete/{id}", produces = "application/json")
    public ResponseEntity<String> deleteCoordonneesPatient(@PathVariable("id") Long id){

       Optional<CoordonneesPatient> coordonneesPatient = coordonneesPatientService.findById(id);
       if(coordonneesPatient.isPresent()){
           LOGGER.info("Suppression du patient {} {}", coordonneesPatient.get().getPrenom(), coordonneesPatient.get().getPrenom());
           coordonneesPatientService.deleteCoordonneesPatient(id);
           return ResponseEntity.ok().body(String.format("Le patient avec l id %s a bien été supprimé", id));
        };
        LOGGER.error("Pas de patient trouvé avec cet id : {}",id);
        throw new PatientNonTrouveException("Pas de patient trouvé avec cet id : " + id);
    }
}
