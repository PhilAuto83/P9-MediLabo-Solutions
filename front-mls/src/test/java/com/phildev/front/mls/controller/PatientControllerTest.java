package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.service.PatientService;
import com.phildev.front.mls.service.UserService;
import com.phildev.front.mls.utils.PrincipalTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private UserService userService;

    private static final Principal PRINCIPAL = new PrincipalTest();



    @Test
    @WithMockUser
    @DisplayName("Récupérer la liste avec 1 patient associé à la structure")
    public void recuperationListePatients() throws Exception {
        when(patientService.recupereToutesLesCoordonneesPatient(any())).thenReturn(List.of(new CoordonneesPatient(1L, 2,"DEV", "PHIL", LocalDate.of(1988,5,21), "M","12 rue du Test", "000-555-7777")));
        mockMvc.perform(get("/patients/liste"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des patients</title>")))
                .andExpect(view().name("patients"))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString(" <td>DEV</td>")))
                .andExpect(content().string(containsString("<td>1988-05-21</td>")))
                .andExpect(content().string(containsString(" <td>M</td>")))
                .andExpect(content().string(containsString("<td>12 rue du Test</td>")))
                .andExpect(content().string(containsString("<td>000-555-7777</td>")))
                .andExpect(content().string(containsString(" <a href=\"/patient/1\">Voir fiche</a>")))
                .andExpect(content().string(containsString("<a href=\"/patient/update/1\">Mettre à jour</a>")))
                .andExpect(content().string(containsString("<a href=\"/patient/suppression/1\">Supprimer</a>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test message d'erreur avec liste vide")
    public void recuperationListePatientsVide() throws Exception {
        when(patientService.recupereToutesLesCoordonneesPatient(any())).thenThrow(ResponseNotFoundException.class);
        mockMvc.perform(get("/patients/liste"))
                .andDo(print())
                .andExpect(content().string(containsString("<title>MLS - Liste des patients</title>")))
                .andExpect(view().name("patients"))
                .andExpect(content().string(containsString("<span class=\"fs-4 text-danger\">La structure n&#39;a aucun patient pour le moment</span>")))
                .andExpect(status().isOk());
    }
}
