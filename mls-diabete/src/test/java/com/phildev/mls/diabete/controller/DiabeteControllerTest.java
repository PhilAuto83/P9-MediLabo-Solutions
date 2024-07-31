package com.phildev.mls.diabete.controller;

import com.phildev.mls.diabete.exception.DonneesPatientNonTrouvees;
import com.phildev.mls.diabete.exception.NoteNonTrouveeException;
import com.phildev.mls.diabete.model.NiveauDeRisque;
import com.phildev.mls.diabete.service.DiabeteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiabeteController.class)
class DiabeteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiabeteService diabeteService;

    @Test
    @DisplayName("Test diagnostic patient en danger")
    void testDiagnosticDiabeteDanger() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenReturn(NiveauDeRisque.DANGER.toString());
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(NiveauDeRisque.DANGER.toString())));
    }

    @Test
    @DisplayName("Test diagnostic patient avec risque limité")
    void testDiagnosticDiabeteRisqueLimite() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenReturn(NiveauDeRisque.LIMITE.toString());
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(NiveauDeRisque.LIMITE.toString())));
    }

    @Test
    @DisplayName("Test diagnostic patient sans risque")
    void testDiagnosticDiabeteSansRisque() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenReturn(NiveauDeRisque.AUCUN.toString());
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(NiveauDeRisque.AUCUN.toString())));
    }

    @Test
    @DisplayName("Test diagnostic patient early on set")
    void testDiagnosticDiabeteEarlyOnSet() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenReturn(NiveauDeRisque.PRECOCE.toString());
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(is(NiveauDeRisque.PRECOCE.toString())));
    }

    @Test
    @DisplayName("Test diagnostic patient avec exception pas de note trouvée")
    void testDiagnosticDiabeteAvecNoteNonTrouveeException() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenThrow(NoteNonTrouveeException.class);
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", is("/diabete/diagnostic/5")))
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    @DisplayName("Test diagnostic patient avec exception coordonnées patient non trouvée")
    void testDiagnosticDiabeteAvecDonneesPatientNonTrouveesException() throws Exception {
        when(diabeteService.calculNiveauRisque(5)).thenThrow(DonneesPatientNonTrouvees.class);
        mockMvc.perform(get("/diabete/diagnostic/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", is("/diabete/diagnostic/5")))
                .andExpect(jsonPath("$.statusCode", is(404)));
    }


}
