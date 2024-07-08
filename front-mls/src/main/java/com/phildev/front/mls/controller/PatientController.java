package com.phildev.front.mls.controller;


import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.service.PatientService;
import com.phildev.front.mls.service.UserService;
import feign.FeignException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Cette classe est un controller qui gère la récupération, la création, la mise à jour et la suppression des patients associés à une structure.
 */
@Controller
public class PatientController {

    private static Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    /**
     * Cette méthode renvoie la liste des patients associés à la structure du user connecté
     * @param principal qui représente le user connecté
     * @return une vue qui renvoie la liste des patients que le user connecté a le droit de gérer
     */
    @GetMapping("/patients/liste")
    public ModelAndView recupereToutesLesCoordonneesPatient(Principal principal){
        ModelAndView mav = new ModelAndView("patients");
        try{
        List<CoordonneesPatient> coordonneesList = patientService.recupereToutesLesCoordonneesPatient(principal);
            mav.addObject("coordonnees_patient", coordonneesList);
            logger.info("Le service mls-coordonnees-patient a retourné une liste de {} patients pour le user {}", coordonneesList.size(), principal.getName());
        }catch(ResponseNotFoundException exception){
            logger.error("Le service mls-coordonnees-patient ne renvoie aucun patient associé au user {}", principal.getName());
            mav.addObject("listeError", "La structure n'a aucun patient pour le moment");
        }
        return mav;
    }

    /**
     * Cette méthode supprime un user par son id
     * @param id qui est l id du user sur le back end mls-coordonnees-patient
     * @param model qui est la vue sur laquelle on renvoie des objets comme les erreurs survenues lors de la suppression
     * @param principal ui représente le user connecté
     * @return la vue des patients
     */
    @GetMapping("/patient/suppression/{id}")
    public String supprimerPatient(@PathVariable("id") Long id, Model model, Principal principal){
        try{
            patientService.supprimerPatient(id);
            logger.info("Le patient {} a bien été supprimé", id);
        }catch (RuntimeException exception){
            model.addAttribute("erreurSuppression", exception.getMessage());
            return "patients";
        }
        return "redirect:/patients/liste";
    }


    /**
     * Cette méthode renvoie la vue qui permet d'ajouter un patient sur la structure en injectant dès l'init le structure id
     * @param model qui permet d'envoyer des objets utilisables sur la vue
     * @param principal qui est le user connecté
     * @return la vue pour l'ajout d'un patient
     */
    @GetMapping("/patient/ajout")
    public String afficherLaVueAjoutPatient(Model model, Principal principal){
        User user  = userService.findByEmail(principal.getName());
        Integer structureId = user.getStructureId();
        CoordonneesPatient patient  = new CoordonneesPatient();
        patient.setStructureId(structureId);
        model.addAttribute("patient", patient);
        return "ajout_patient";
    }

    /**
     * Cette méthode permet de créer un patient ou de renvoyer d'éventuelles erreurs si les champs sont mal remplis
     * @param coordonneesPatient qui représente les coordonnées d'un patient
     * @param result qui contient le résultat de la validation des données renseignées
     * @param model qui renvoie des objets à la vue
     * @return une vue qui est la liste des patients mise à jour si le résultat est ok ou la page actuelle avec des messages d'erreur si des champs sont mal renseignés
     */
    @PostMapping(value = "/patient", consumes = "application/x-www-form-urlencoded")
    public String ajouterUnPatient(@ModelAttribute("patient") @Valid CoordonneesPatient coordonneesPatient, BindingResult result, Model model){
        if(result.hasErrors()) {
            if(result.hasFieldErrors("dateDeNaissance")){
                model.addAttribute("dateError", "Le format de la date n'est pas valide, il doit respecter le format yyyy-MM-dd");
            }
            return "ajout_patient";
        } else{
            if(StringUtils.isNotBlank(coordonneesPatient.getTelephone()) && !Pattern.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}", coordonneesPatient.getTelephone())){
                logger.error("Le numéro de téléphone doit respecter le format suivant 111-444-7777");
                model.addAttribute("telephoneError", "Le numéro de téléphone doit respecter le format suivant 111-444-7777");
                return "ajout_patient";
            }else{
                logger.info("Pas d'erreur sur la validation des champs de création du patient {} {}", coordonneesPatient.getPrenom(), coordonneesPatient.getNom());
            }
        }
        try{
            CoordonneesPatient patient = patientService.sauvegarderUnPatient(coordonneesPatient);
            logger.info("Le patient {}]{} a été ajouté à la structure n° {} ", patient.getPrenom(), patient.getNom(), patient.getStructureId());
            return "redirect:/patients/liste";

        }catch(ResponseNotFoundException | BadRequestException exception){
            logger.error(exception.getMessage());
            model.addAttribute("backendError", exception.getMessage());
            return "ajout_patient";
        }
    }

    /**
     * Cette méthode permet de renvoyer l'utilisateur sur la page de modification des coordonnées du patient sélectionné
     * @param id qui est l'id du patient sélectionné
     * @param model qui permet de renvoyer un objet {@link CoordonneesPatient} dans la vue update_patient.html
     * @return la vue update_patient.html
     */
    @GetMapping("/patient/update/{id}")
    public String afficherLaVueAjoutPatientAvecLesInfosPatient(@PathVariable("id") Long id, Model model){

        try{
            CoordonneesPatient patient  = patientService.recuperePatient(id);
            model.addAttribute("patient", patient);
            logger.info("Patient {} {} prêt à être mis à jour", patient.getPrenom(), patient.getNom());
            return "update_patient";
        }catch(ResponseNotFoundException exception){
            model.addAttribute("patientError", exception.getMessage());
            logger.error("Le patient avec l'id {} n' a pas été trouvé ", id);
            return "redirect:/patients/liste";
        }
    }

    /**
     * Cette méthode permet de mettre à jour un patient via son id
     * @param id qui est l'identifiant du patient en base de données
     * @param coordonneesPatient qui représente les nouvelles coordonnées du patient
     * @param result qui permet de vérifier la validité des données
     * @param model qui renvoie des objets dans la vue patients.html ou dans la vue update_patient.html
     * @return la vue de la liste patients si tout est OK sinon reste sur la vue en cours avec des erreurs
     */
    @PostMapping(value = "/patient/update/{id}", consumes = "application/x-www-form-urlencoded")
    public String updateDuPatient(@PathVariable("id") Long id, @Valid @ModelAttribute("patient") CoordonneesPatient coordonneesPatient, BindingResult result, Model model){
        if(result.hasErrors()) {
            if(result.hasFieldErrors("dateDeNaissance")){
                model.addAttribute("dateError", "Le format de la date n'est pas valide, il doit respecter le format yyyy-MM-dd");
            }
            return "update_patient";
        } else{
            if(StringUtils.isNotBlank(coordonneesPatient.getTelephone()) && !Pattern.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}", coordonneesPatient.getTelephone())){
                logger.error("Le numéro de téléphone doit respecter le format suivant 111-444-7777");
                model.addAttribute("telephoneError", "Le numéro de téléphone doit respecter le format suivant 111-444-7777");
                return "update_patient";
            }else{
                logger.info("Pas d'erreur sur la validation des champs de création du patient {} {}", coordonneesPatient.getPrenom(), coordonneesPatient.getNom());
            }
        }
        try{
            coordonneesPatient.setId(id);
            CoordonneesPatient patient = patientService.sauvegarderUnPatient(coordonneesPatient);
            logger.info("Le patient {}]{} a été mis à jour à la structure n° {} ", patient.getPrenom(), patient.getNom(), patient.getStructureId());
            return "redirect:/patients/liste";

        }catch(FeignException exception){
            logger.error(exception.getMessage());
            model.addAttribute("backendError", exception.getMessage());
            return "update_patient";
        }
    }
}
