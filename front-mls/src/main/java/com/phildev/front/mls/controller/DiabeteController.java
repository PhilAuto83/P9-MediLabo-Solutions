package com.phildev.front.mls.controller;

import com.phildev.front.mls.service.DiabeteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("diabete")
public class DiabeteController {
    private final DiabeteService diabeteService;
    public DiabeteController(DiabeteService diabeteService) {
        this.diabeteService = diabeteService;
    }

    @GetMapping("/diagnostic/{patientId")
    public String afficherLeDiagnosticDiabete(@PathVariable("patientId") Integer patientId){
        log.info("Récupération des informations du patient {} pour le calcul de son niveau de risque diabète", patientId);
        return diabeteService.calculDiagnosticDiabete(patientId);
    }
}
