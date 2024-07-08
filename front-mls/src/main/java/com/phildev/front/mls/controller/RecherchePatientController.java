package com.phildev.front.mls.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cette classe permet de rechercher un patient via son nom et fournit des endpoints pour renvoyer la liste des patients avec le nom recherché
 */
@Controller
public class RecherchePatientController {

    @GetMapping("patient/recherche")
    public String afficheLaRecherchePatient(){
        return "recherche_patient";
    }
}
