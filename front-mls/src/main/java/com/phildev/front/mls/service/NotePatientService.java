package com.phildev.front.mls.service;

import com.phildev.front.mls.model.NotePatient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotePatientService {
    private final MicroserviceNotesPatientProxy microserviceNotesPatientProxy;
    public NotePatientService(MicroserviceNotesPatientProxy microserviceNotesPatientProxy) {
        this.microserviceNotesPatientProxy = microserviceNotesPatientProxy;
    }

    public Page<NotePatient> recupererLesNotesParPatient(Integer patientId, Integer pageNo){
        Page<NotePatient> notes = microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(patientId, pageNo);
        log.info("Nombre total d'éléments récupérés : {}", notes.getTotalElements());
        return notes;
    }

    public void supprimerLaNote(String id) {
        ResponseEntity<String> response = microserviceNotesPatientProxy.supprimerLaNote(id);
        log.info(response.getBody());
    }
}
