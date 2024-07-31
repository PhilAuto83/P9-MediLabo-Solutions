package com.phildev.mls.notes.patient.controller;

import com.phildev.mls.notes.patient.exception.NoteNonSupprimeeException;
import com.phildev.mls.notes.patient.model.NotePatient;
import com.phildev.mls.notes.patient.service.NotePatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class NotePatientController {

    private static final Logger logger = LoggerFactory.getLogger(NotePatientController.class);

    @Autowired
    private NotePatientService notePatientService;

    @GetMapping("/patient/notes")
    public Page<NotePatient> recupererLesNotesParPatientParPaqe(@RequestParam("patientId") @NotNull(message ="l'id du patient ne peut pas être null") Integer patientId, @RequestParam("pageNo") Integer pageNo){
        return notePatientService.recupererLesNotesParPatientParPage(patientId, pageNo);
    }

    @GetMapping("/patient/notes/all")
    public List<NotePatient> recupererToutesLesNotesParPatient(@RequestParam("patientId") @NotNull(message ="l'id du patient ne peut pas être null") Integer patientId){
        return notePatientService.recupererToutesLesNotesParPatient(patientId);
    }

    @PostMapping("/patient/note")
    public NotePatient ajouterUneNote(@Valid @RequestBody NotePatient note){
        return notePatientService.ajouterUneNote(note);
    }

    @DeleteMapping("/patient/note/{id}")
    public ResponseEntity<String> supprimerLaNote(@PathVariable("id") String id){
        notePatientService.supprimerLaNote(id);
        Optional<NotePatient> notePatient = notePatientService.trouverLaNote(id);
        if(notePatient.isPresent()){
            logger.error("La note avec l'id {} n'a pas été supprimée", id);
            throw new NoteNonSupprimeeException("La note avec l'id "+id+" n'a pas été supprimée");

        }else{
            logger.info("La note du patient avec l'id {} a bien été supprimée", id);
            return ResponseEntity.ok("La note du patient avec l'id "+id+ " a été supprimée avec succès");
        }
    }
}
