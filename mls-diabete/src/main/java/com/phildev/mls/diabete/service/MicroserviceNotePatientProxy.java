package com.phildev.mls.diabete.service;

import com.phildev.mls.diabete.model.NotePatient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Cette interface permet grâce à Feign d'appeler le micro service gateway qui lui appelle le microservice back-end mls-notes-patient
 */
@FeignClient(name ="mls-gateway-notes")
public interface MicroserviceNotePatientProxy {
    @GetMapping("/gateway/patient/notes/all")
    public List<NotePatient> recupererLesNotesParPatient(@RequestParam("patientId") Integer patientId);
}
