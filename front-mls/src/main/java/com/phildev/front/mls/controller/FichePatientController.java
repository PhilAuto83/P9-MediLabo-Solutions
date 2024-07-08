package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.service.FichePatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cette classe est un controller qui gère l'affichage des données d'un patient comme son nom, prénom, âge
 * mais aussi l'historique des notes sur le patient ainsi que la génération du rapport de diabète.
 *
 */

@Controller
public class FichePatientController {

    private static final Logger logger = LoggerFactory.getLogger(FichePatientController.class);

    @Autowired
    private FichePatientService fichePatientService;


    @GetMapping("patient/fiche/{id}")
    public String afficheLesInfosPatient(@PathVariable("id") Long id, Model model){
        try{
            FichePatient fichePatient = fichePatientService.recupereLaFichePatient(id);
            model.addAttribute("fiche", fichePatient);
            return "fiche_patient";
        }catch(ResponseNotFoundException responseNotFoundException){
            logger.error("Le patient n'a pas été trouvé avec son id {}", id);
            model.addAttribute("patientErreur", "Le patient n'a pas été trouvé avec son id "+id);
            return "recherche_patient";
        }
    }
}
