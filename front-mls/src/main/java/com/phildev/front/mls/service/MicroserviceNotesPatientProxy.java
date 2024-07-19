package com.phildev.front.mls.service;

import com.phildev.front.mls.model.NotePatient;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-notes-patient
 */
@FeignClient(name ="mls-gateway-notes")
public interface MicroserviceNotesPatientProxy {
    @GetMapping("/gateway/patient/notes")
    public Page<NotePatient> recupererLesNotesParPatientParPaqe(@RequestParam("patientId") Integer patientId, @RequestParam("pageNo") Integer pageNo);

    @PostMapping("/gateway/patient/note")
    public NotePatient ajouterUneNote(@Valid @RequestBody NotePatient note);

    @DeleteMapping("/gateway/patient/note/{id}")
    public ResponseEntity<String> supprimerLaNote(@PathVariable("id") String id);
}
