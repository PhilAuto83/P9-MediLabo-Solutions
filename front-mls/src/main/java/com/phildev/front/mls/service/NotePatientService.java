package com.phildev.front.mls.service;

import com.phildev.front.mls.model.NotePatient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;

@Service
public class NotePatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotePatientService.class);

    @Autowired
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    public Page<NotePatient> recupererLesNotesParPatient(Integer patientId, Integer pageNo){
        Page<NotePatient> notes = microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(patientId, pageNo);
        LOGGER.info("Nombre total d'éléments récupérés : {}", notes.getTotalElements());
        return notes;
    }

    public void supprimerLaNote(String id) {
        ResponseEntity<String> response = microserviceNotesPatientProxy.supprimerLaNote(id);
        LOGGER.info(response.getBody());
    }
}
