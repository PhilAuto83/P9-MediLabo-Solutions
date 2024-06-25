package com.phildev.front.mls.controller;


import com.phildev.front.mls.dto.CoordonneesDTO;
import com.phildev.front.mls.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<CoordonneesDTO> coordonneesDTOList = patientService.recupereToutesLesCoordonneesPatient(principal);
        mav.addObject("coordonnees_patient", coordonneesDTOList);
        return mav;
    }
}
