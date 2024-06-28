package com.phildev.front.mls.controller;


import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

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
}
