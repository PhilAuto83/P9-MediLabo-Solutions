package com.phildev.front.mls.service;

import com.phildev.front.mls.model.NotePatient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class NotePatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotePatientService.class);


    @Autowired
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    public Page<NotePatient> recupererLesNotesParPatient(Integer patientId, Integer pageNo){
        return microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(patientId, pageNo);
    }

    public void supprimerLaNote(String id) {
        microserviceNotesPatientProxy.supprimerLaNote(id);
    }
}
