package com.phildev.mls.notes.patient.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phildev.mls.notes.patient.exception.NoteNonTrouveeException;
import com.phildev.mls.notes.patient.model.NotePatient;
import com.phildev.mls.notes.patient.service.NotePatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(NotePatientController.class)
public class NotePatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotePatientService notePatientService;
    @Test
    @DisplayName("test de récupération des notes d'un patient avec page")
    void testRecuperationNotesPatientAvecPage() throws Exception {
        Page<NotePatient> notes = new PageImpl<>(List.of(new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", "First note")), PageRequest.of(0, 5), 2);
        when(notePatientService.recupererLesNotesParPatientParPage(5, 0)).thenReturn(notes);
        mockMvc.perform(get("/patient/notes")
                .param("patientId", "5")
                .param("pageNo", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].patient", is("PHIL DEV")))
                .andExpect(jsonPath("$.content[0].note", is("First note")));
    }

    @Test
    @DisplayName("test de récupération des notes d'un patient")
    void testRecuperationNotesPatient() throws Exception {
        List<NotePatient> notes = List.of(new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", "First note"));
        when(notePatientService.recupererToutesLesNotesParPatient(5)).thenReturn(notes);
        mockMvc.perform(get("/patient/notes/all")
                        .param("patientId", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patient", is("PHIL DEV")))
                .andExpect(jsonPath("$[0].note", is("First note")));
    }

    @Test
    @DisplayName("test retour 404 avec NoteNonTrouveeException")
    void testNoteNonTrouveeException() throws Exception {
        when(notePatientService.recupererToutesLesNotesParPatient(5)).thenThrow( new NoteNonTrouveeException("Aucune note pour le patient"));
        mockMvc.perform(get("/patient/notes/all")
                        .param("patientId", "5"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Aucune note pour le patient")));
    }

    @Test
    @DisplayName("test de création d'une note patient")
    void testAjouterUneNote() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", "First note");
        when(notePatientService.ajouterUneNote(note)).thenReturn(note);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(note)))
                .andDo(print())
                .andExpect(jsonPath("$.note", is("First note")))
                .andExpect(jsonPath("$.patient", is("PHIL DEV")))
                .andExpect(jsonPath("$.patientId", is(5)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tester l'ajout d'une note vide qui renvoie un code 400")
    void testAjouterUneNoteVideRenvoieUneBadRequest() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", "");
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(note)))
                .andDo(print())
                .andExpect(jsonPath("$.message", containsString("la note ne peut pas être nulle ou vide")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tester l'ajout d'une note nulle qui renvoie un code 400")
    void testAjouterUneNoteNulleRenvoieUneBadRequest() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", null);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(note)))
                .andDo(print())
                .andExpect(jsonPath("$.message", containsString("la note ne peut pas être nulle ou vide")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tester l'ajout d'une note avec des espaces vides qui renvoie un code 400")
    void testAjouterUneNoteAvecEspacesVidesRenvoieUneBadRequest() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),5,"PHIL DEV", "  ");
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc.perform(post("/patient/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(note)))
                .andDo(print())
                .andExpect(jsonPath("$.message", containsString("la note ne peut pas être nulle ou vide")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Tester la suppression d'une note patient")
    void testSuppressionNotePatient() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),10,"PHIL DEV", "Ok");
        when(notePatientService.trouverLaNote("123456a")).thenReturn(Optional.empty());
        mockMvc.perform(delete("/patient/note/123456a"))
                .andDo(print())
                .andExpect(content().string("La note du patient avec l'id 123456a a été supprimée avec succès"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tester la suppression d'une note patient qui échoue")
    void testSuppressionNotePatientEchoue() throws Exception {
        NotePatient note = new NotePatient("123456a", LocalDateTime.of(2024,7,16,14,52,10),10,"PHIL DEV", "Ok");
        when(notePatientService.trouverLaNote("123456a")).thenReturn(Optional.of(note));
        mockMvc.perform(delete("/patient/note/123456a"))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("La note avec l'id 123456a n'a pas été supprimée")))
                .andExpect(status().isBadRequest());
    }

}
