package com.phildev.front.mls.controller;


import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.service.PatientService;
import com.phildev.front.mls.service.UserService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

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
        List<CoordonneesPatient> coordonneesList = patientService.recupereToutesLesCoordonneesPatient(principal);
        if(coordonneesList.isEmpty()){
            mav.addObject("listeVide", "La structure n'a aucun patient pour le moment");
        }else{
            mav.addObject("coordonnees_patient", coordonneesList);
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
        } try{
            CoordonneesPatient patient = patientService.ajouterUnPatient(coordonneesPatient);
            logger.info("Le patient {}]{} a été ajouté à la structure n° {} ", patient.getPrenom(), patient.getNom(), patient.getStructureId());
            return "redirect:/patients/liste";

        }catch(FeignException exception){
            logger.error(exception.getMessage());
            model.addAttribute("backendError", exception.getMessage());
            return "ajout_patient";
        }
    }
}
