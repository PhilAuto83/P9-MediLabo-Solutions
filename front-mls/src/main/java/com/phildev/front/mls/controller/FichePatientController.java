package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.DiagnosticNotFoundException;
import com.phildev.front.mls.error.FichePatientNotFoundException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.service.DiabeteService;
import com.phildev.front.mls.service.FichePatientService;
import com.phildev.front.mls.service.NotePatientService;
import com.phildev.front.mls.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Cette classe est un controller qui gère l'affichage des données d'un patient comme son nom, prénom, âge
 * mais aussi l'historique des notes sur le patient ainsi que la génération du rapport de diabète.
 *
 */
@Controller
@Slf4j
public class FichePatientController {
    private final FichePatientService fichePatientService;
    private final NotePatientService notePatientService;

    private final DiabeteService diabeteService;

    public FichePatientController(FichePatientService fichePatientService, NotePatientService notePatientService, DiabeteService diabeteService) {
        this.fichePatientService = fichePatientService;
        this.notePatientService = notePatientService;
        this.diabeteService = diabeteService;
    }


    @GetMapping(value = "patient/fiche/{id}/pageNo/{pageNo}")
    public String afficheLesInfosPatient(@PathVariable("id") Long id, @PathVariable("pageNo") Integer pageNo, Model model, HttpServletRequest request){
        NotePatient nouvelleNote = new NotePatient();
        model.addAttribute("note", nouvelleNote);
        Utility.ajoutAttributErreur(model, "ajoutNoteErreur", "ajoutNoteErreur");
        try{
            int pageActuelle = pageNo!=null?pageNo:0;
            model.addAttribute("pageActuelle", pageActuelle);
            FichePatient fichePatient = fichePatientService.recupereLaFichePatient(id);
            model.addAttribute("fiche", fichePatient);
            nouvelleNote.setPatientId(fichePatient.getId().intValue());
            nouvelleNote.setPatient(String.format("%s %s", fichePatient.getPrenom(), fichePatient.getNom()));
            Page<NotePatient> notes = notePatientService.recupererLesNotesParPatient(id.intValue(), pageActuelle);
            model.addAttribute("notes", notes);
            String diabeteDiagnostic = diabeteService.calculDiagnosticDiabete(id.intValue());
            model.addAttribute("diabeteDiagnostic", diabeteDiagnostic);
        }catch(BadRequestException | ResponseNotFoundException responseNotFoundException) {
            model.addAttribute("noteErreur", "Le patient n'a pas encore de notes");
        }catch(FichePatientNotFoundException exception){
            log.error("Le patient n'a pas été trouvé avec son id {}", id);
            model.addAttribute("patientErreur", String.format("Le patient n'a pas été trouvé avec son id %s ",id));
        }catch(DiagnosticNotFoundException exception){
            model.addAttribute("diagnosticErreur", "Les informations pour calculer le rapport sont insuffisantes");
        }
        return "fiche_patient";
    }

    @GetMapping("patient/{patientId}/note/{id}")
    public String supprimerNotePatient(@PathVariable("patientId") Integer patientId, @PathVariable("id")String id, RedirectAttributes model){
        try{
            notePatientService.supprimerLaNote(id);
            log.info("La note {} a bien été supprimée", id);
        }catch(BadRequestException |ResponseNotFoundException exception){
            log.error(exception.getMessage());
            model.addFlashAttribute("noteSuppressionErreur", exception.getMessage());
        }
        return "redirect:/patient/fiche/"+patientId+"/pageNo/0";
    }

    @PostMapping(value = "patient/note", consumes = "application/x-www-form-urlencoded")
    public String ajouterUneNote(@ModelAttribute("note")@Valid NotePatient notePatient, BindingResult result, RedirectAttributes model){
        if(result.hasErrors()){
            log.error("Le champ du formulaire {} n' a pas été correctement rempli", result.getFieldError().getField());
            model.addFlashAttribute("ajoutNoteErreur", result.getFieldError().getDefaultMessage());
            return "redirect:/patient/fiche/"+notePatient.getPatientId()+"/pageNo/0";
        }
        try{
            notePatient.setDateCreation(LocalDateTime.now());
            NotePatient  noteSaved = fichePatientService.ajouterUneNote(notePatient);
            log.info("La note {} a été sauvegardée avec succès pour le patient {}", noteSaved.getId(), notePatient.getPatient());
        }catch(BadRequestException | ResponseNotFoundException exception){
            log.error(exception.getMessage());
        }
        return "redirect:/patient/fiche/"+notePatient.getPatientId()+"/pageNo/0";
    }
}
