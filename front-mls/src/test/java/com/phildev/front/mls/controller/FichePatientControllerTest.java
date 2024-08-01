package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.BadRequestException;
import com.phildev.front.mls.error.DiagnosticNotFoundException;
import com.phildev.front.mls.error.FichePatientNotFoundException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest({FichePatientController.class, FichePatientService.class, NotePatientService.class, DiabeteService.class})
class FichePatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    @MockBean
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    @MockBean
    private MicroserviceDiabeteProxy microserviceDiabeteProxy;

    @Test
    @WithMockUser
    @DisplayName("Test fiche patient avec calcul de l'âge du patient, niveau de risque In Danger et affichage d'une note")
    void testFichePatientAvecCalculAgePatientAvecNiveauRisqueNone() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);

        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,0)).thenReturn(notes);
        when(microserviceDiabeteProxy.calculNiveauRisqueDiabete(1)).thenReturn("None");
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(view().name("fiche_patient"))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Informations générales</div>")))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString("<td>TEST</td>")))
                .andExpect(content().string(containsString("<td>34</td>")))
                .andExpect(content().string(containsString("<td>15 rue des tests</td>")))
                .andExpect(content().string(containsString("<td>000-555-9999</td>")))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Historique du patient</div>")))
                .andExpect(content().string(containsString("<td style=\"white-space: pre-line\" class=\"col-8\">First note</td>")))
                .andExpect(content().string(containsString("<a href=\"/patient/1/note/123456a\">Supprimer</a>")))
                .andExpect(content().string(containsString("<p class=\"text-center\">Le niveau de risque diabète pour le patient est : <span class=\"text-info fs-6\">None</span></p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test fiche patient avec calcul de l'âge du patient, affichage d'une note et exceptioon sur l'appel au diabete service")
    void testFichePatientAvecExceptionAppelDIabeteService() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);

        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,0)).thenReturn(notes);
        when(microserviceDiabeteProxy.calculNiveauRisqueDiabete(1)).thenThrow(DiagnosticNotFoundException.class);
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(view().name("fiche_patient"))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Informations générales</div>")))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString("<td>TEST</td>")))
                .andExpect(content().string(containsString("<td>34</td>")))
                .andExpect(content().string(containsString("<td>15 rue des tests</td>")))
                .andExpect(content().string(containsString("<td>000-555-9999</td>")))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Historique du patient</div>")))
                .andExpect(content().string(containsString("<td style=\"white-space: pre-line\" class=\"col-8\">First note</td>")))
                .andExpect(content().string(containsString("<a href=\"/patient/1/note/123456a\">Supprimer</a>")))
                .andExpect(content().string(containsString("<p class=\"text-danger text-center\">Les informations pour calculer le rapport sont insuffisantes</p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test fiche patient avec calcul de l'âge du patient, niveau de risque In Danger et affichage d'une note")
    void testFichePatientAvecCalculAgePatientEtNiveauRisqueInDanger() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);

        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,0)).thenReturn(notes);
        when(microserviceDiabeteProxy.calculNiveauRisqueDiabete(1)).thenReturn("In Danger");
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(view().name("fiche_patient"))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Informations générales</div>")))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString("<td>TEST</td>")))
                .andExpect(content().string(containsString("<td>34</td>")))
                .andExpect(content().string(containsString("<td>15 rue des tests</td>")))
                .andExpect(content().string(containsString("<td>000-555-9999</td>")))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-4\">Historique du patient</div>")))
                .andExpect(content().string(containsString("<td style=\"white-space: pre-line\" class=\"col-8\">First note</td>")))
                .andExpect(content().string(containsString("<a href=\"/patient/1/note/123456a\">Supprimer</a>")))
                .andExpect(content().string(containsString("<p class=\"text-center\">Le niveau de risque diabète pour le patient est : <span class=\"text-info fs-6\">In Danger</span></p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test message d'erreur sur fiche patient si le service proxy renvoie une erreur")
    void testFichePatientAvecErreurSiProxyRenvoieUneErreur() throws Exception {
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenThrow(FichePatientNotFoundException.class);
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(model().attributeExists("patientErreur"))
                .andExpect(content().string(containsString("Le patient n&#39;a pas été trouvé avec son id 1")))
                .andExpect(content().string(containsString("<p class=\"text-center text-info\">Les informations actuelles ne sont pas suffisantes pour calculer le niveau de risque</p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test message d'erreur sur note patient si le service proxy renvoie une erreur")
    void testNotePatientSiProxyRenvoieUneErreur() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,  0)).thenThrow(ResponseNotFoundException.class);
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(model().attributeExists("noteErreur"))
                .andExpect(content().string(containsString("<span class=\"text-danger\" style=\"font-size: 1rem\">Le patient n&#39;a pas encore de notes</span>")))
                .andExpect(content().string(containsString("<p class=\"text-center text-info\">Les informations actuelles ne sont pas suffisantes pour calculer le niveau de risque</p>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test suppression d'une note")
    void testSuppressionNotePatient() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,  0)).thenReturn(notes);
        when(microserviceNotesPatientProxy.supprimerLaNote("123456a")).thenReturn(ResponseEntity.ok("La note a été supprimée"));
        mockMvc.perform(get("/patient/1/note/123456a"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/fiche/1/pageNo/0"))
                .andExpect(redirectedUrl("/patient/fiche/1/pageNo/0"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test suppression d'une note avec erreur BadRequestException")
    void testSuppressionNotePatientErreur400() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,  0)).thenReturn(notes);
        when(microserviceNotesPatientProxy.supprimerLaNote("123456a")).thenThrow(new BadRequestException("Bad request"));
        mockMvc.perform(get("/patient/1/note/123456a"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/fiche/1/pageNo/0"))
                .andExpect(redirectedUrl("/patient/fiche/1/pageNo/0"))
                .andExpect(flash().attribute("noteSuppressionErreur", "Bad request"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test suppression d'une note avec erreur ResponseNotFoundException")
    void testSuppressionNotePatientErreur404() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(1,  0)).thenReturn(notes);
        when(microserviceNotesPatientProxy.supprimerLaNote("123456a")).thenThrow(new ResponseNotFoundException("La note n'existe pas"));
        mockMvc.perform(get("/patient/1/note/123456a"))
                .andDo(print())
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/patient/fiche/1/pageNo/0"))
                .andExpect(redirectedUrl("/patient/fiche/1/pageNo/0"))
                .andExpect(flash().attribute("noteSuppressionErreur", "La note n'existe pas"));
    }

    @Test
    @WithMockUser
    @DisplayName("Ajout d'une note vide pour le patient")
    void testAjoutNotePatientVide() throws Exception {
        mockMvc.perform(post("/patient/note")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateCreation", (String) null)
                        .param("note", (String) null)
                        .param("patientId", "8")
                        .param("patient", "Phil Developer"))
                .andDo(print())
                .andExpect(flash().attributeExists("ajoutNoteErreur"))
                .andExpect(redirectedUrl("/patient/fiche/8/pageNo/0"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser
    @DisplayName("Ajout d'une note pour un patient")
    void testAjoutNotePatientValide() throws Exception {
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),8,"Phil Developer","First note");
        when(microserviceNotesPatientProxy.ajouterUneNote(any(NotePatient.class))).thenReturn(notePatient);
        mockMvc.perform(post("/patient/note")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateCreation", (String) null)
                        .param("note", "First note")
                        .param("patientId", "8")
                        .param("patient", "Phil Developer"))
                .andDo(print())
                .andExpect(redirectedUrl("/patient/fiche/8/pageNo/0"))
                .andExpect(status().is(302));
    }

    @Test
    @WithMockUser
    @DisplayName("Test appel du microservice note patient avec retour BadRequestException")
    void testAjoutNotePatientAvecRetourBadRequestException() throws Exception {
        when(microserviceNotesPatientProxy.ajouterUneNote(any(NotePatient.class))).thenThrow(new BadRequestException("erreur sur le formulaire"));
        mockMvc.perform(post("/patient/note")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateCreation", (String) null)
                        .param("note", "First note")
                        .param("patientId", "8")
                        .param("patient", "Phil Developer"))
                .andDo(print())
                .andExpect(redirectedUrl("/patient/fiche/8/pageNo/0"))
                .andExpect(status().is(302));

    }

    @Test
    @WithMockUser
    @DisplayName("Test appel du microservice note patient avec retour ResponseNotFoundException")
    void testAjoutNotePatientAvecRetourResponseNotFoundException() throws Exception {
        when(microserviceNotesPatientProxy.ajouterUneNote(any(NotePatient.class))).thenThrow(new ResponseNotFoundException("erreur sur le formulaire"));;
        mockMvc.perform(post("/patient/note")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dateCreation", (String) null)
                        .param("note", "First note")
                        .param("patientId", "8")
                        .param("patient", "Phil Developer"))
                .andDo(print())
                .andExpect(redirectedUrl("/patient/fiche/8/pageNo/0"))
                .andExpect(status().is(302));
    }
}
