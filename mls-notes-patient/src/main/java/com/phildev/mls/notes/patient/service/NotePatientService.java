package com.phildev.mls.notes.patient.service;


import com.phildev.mls.notes.patient.exception.NoteNonTrouveeException;
import com.phildev.mls.notes.patient.model.NotePatient;
import com.phildev.mls.notes.patient.repository.NotePatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotePatientService {

    private static final Logger logger = LoggerFactory.getLogger(NotePatientService.class);


    @Autowired
    private NotePatientRepository notePatientRepository;


    public Page<NotePatient> recupererLesNotesParPatientParPage(Integer patientId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("creation_date"));
        Page<NotePatient> notes = notePatientRepository.findByPatientIdWithPage(patientId, pageable);
        if(notes.isEmpty()){
            logger.error("Aucune note trouvée avec l'id patient {} pour la page {}", patientId, pageNumber);
            throw new NoteNonTrouveeException("Aucune note de trouver pour le patient avec l'id "+ patientId+ " pour la page "+pageNumber);
        }else{
            return notes;
        }
    }

    public NotePatient ajouterUneNote(NotePatient note) {
        return notePatientRepository.save(note);
    }

    public Optional<NotePatient> trouverLaNote(String id){
        return notePatientRepository.findById(id);
    }

    public void supprimerLaNote(String id) {
        notePatientRepository.deleteById(id);
    }
}
