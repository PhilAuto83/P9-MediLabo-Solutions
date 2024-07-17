package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.service.FichePatientService;
import com.phildev.front.mls.service.NotePatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        try{
            int pageActuelle = pageNo!=null?pageNo:0;
            FichePatient fichePatient = fichePatientService.recupereLaFichePatient(id);
            Page<NotePatient> notes = notePatientService.recupererLesNotesParPatient(id.intValue(), pageActuelle);
            model.addAttribute("pageActuelle", pageActuelle);
            model.addAttribute("fiche", fichePatient);
            model.addAttribute("notes", notes);
        }catch(ResponseNotFoundException responseNotFoundException){
            logger.error("Le patient n'a pas été trouvé avec son id {}", id);
            model.addAttribute("patientErreur", "Le patient n'a pas été trouvé avec son id "+id);
            model.addAttribute("noteErreur", "Le patient n'a pas encore de notes");
        }
        return "fiche_patient";
    }

    @GetMapping("patient/{patientId}/note/{id}")
    public String supprimerNotePatient(@PathVariable("patientId") Integer patientId, @PathVariable("id")String id, Model model){
        try{
            notePatientService.supprimerLaNote(id);
            logger.info("La note {} a bien été supprimée", id);
        }catch(BadRequestException exception){
            logger.error(exception.getMessage());
            model.addAttribute("noteErreur", exception.getMessage());
        }
        return "redirect:/patient/fiche/"+patientId;
    }
}
