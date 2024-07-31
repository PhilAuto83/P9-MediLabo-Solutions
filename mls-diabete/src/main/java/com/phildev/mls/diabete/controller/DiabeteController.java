package com.phildev.mls.diabete.controller;


import com.phildev.mls.diabete.service.DiabeteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/diabete")
public class DiabeteController {
    private final DiabeteService diabeteService;

    public DiabeteController(DiabeteService diabeteService) {
        this.diabeteService = diabeteService;
    }

    @GetMapping("/diagnostic/{patientId}")
    public String getDiabeteDiagnostic(@PathVariable("patientId") Integer patientId){
        log.info("Récupération du diagnostic diabète pour le patient {}", patientId);
        return diabeteService.calculNiveauRisque(patientId);
    }
}
