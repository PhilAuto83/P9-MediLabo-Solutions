package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.FichePatientNotFoundException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.service.FichePatientService;
import com.phildev.front.mls.service.NotePatientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Cette classe est un controller qui gère l'affichage des données d'un patient comme son nom, prénom, âge
 * mais aussi l'historique des notes sur le patient ainsi que la génération du rapport de diabète.
 *
 */
@Controller
public class FichePatientController {

    private static final Logger logger = LoggerFactory.getLogger(FichePatientController.class);

    @Autowired
    private FichePatientService fichePatientService;

    @Autowired
    private NotePatientService notePatientService;


    @GetMapping("patient/fiche/{id}/pageNo/{pageNo}")
    public String afficheLesInfosPatient(@PathVariable("id") Long id, @PathVariable("pageNo") Integer pageNo, Model model){
        NotePatient nouvelleNote = new NotePatient();
        model.addAttribute("note", nouvelleNote);
        try{
            int pageActuelle = pageNo!=null?pageNo:0;
            model.addAttribute("pageActuelle", pageActuelle);
            FichePatient fichePatient = fichePatientService.recupereLaFichePatient(id);
            model.addAttribute("fiche", fichePatient);
            nouvelleNote.setPatientId(fichePatient.getId().intValue());
            nouvelleNote.setPatient(String.format("%s %s", fichePatient.getPrenom(), fichePatient.getNom()));
            Page<NotePatient> notes = notePatientService.recupererLesNotesParPatient(id.intValue(), pageActuelle);
            model.addAttribute("notes", notes);
        }catch(ResponseNotFoundException responseNotFoundException){
            model.addAttribute("noteErreur", "Le patient n'a pas encore de notes");
        }catch(FichePatientNotFoundException exception){
            logger.error("Le patient n'a pas été trouvé avec son id {}", id);
            model.addAttribute("patientErreur", "Le patient n'a pas été trouvé avec son id "+id);
        }
        return "fiche_patient";
    }

    @GetMapping("patient/{patientId}/note/{id}")
    public String supprimerNotePatient(@PathVariable("patientId") Integer patientId, @PathVariable("id")String id, Model model){
        try{
            notePatientService.supprimerLaNote(id);
            logger.info("La note {} a bien été supprimée", id);
        }catch(BadRequestException |ResponseNotFoundException exception){
            logger.error(exception.getMessage());
            model.addAttribute("noteErreur", exception.getMessage());
        }
        return "redirect:/patient/fiche/"+patientId+"/pageNo/0";
    }

    @PostMapping(value = "patient/note", consumes = "application/x-www-form-urlencoded")
    public String ajouterUneNote(@ModelAttribute("note")@Valid NotePatient notePatient, BindingResult result, RedirectAttributes model){
        if(result.hasErrors()){
            logger.error("Le champ du formulaire {} n' a pas été correctement rempli", result.getFieldError().getField());
        }
        try{
            notePatient.setDateCreation(LocalDateTime.now());
            NotePatient  noteSaved = fichePatientService.ajouterUneNote(notePatient);
            logger.info("La note {} a été sauvegardée avec succès pour le patient {}", noteSaved.getId(), notePatient.getPatient());
        }catch(BadRequestException |ResponseNotFoundException exception){
            logger.error(exception.getMessage());
            model.addFlashAttribute("noteErreur", exception.getMessage());
        }
        return "redirect:/patient/fiche/"+notePatient.getPatientId()+"/pageNo/0";
    }
}
