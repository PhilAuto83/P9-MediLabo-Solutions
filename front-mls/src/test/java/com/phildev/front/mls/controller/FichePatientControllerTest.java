package com.phildev.front.mls.controller;

import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.service.FichePatientService;
import com.phildev.front.mls.service.MicroserviceCoordonneesPatientProxy;
import com.phildev.front.mls.service.MicroserviceNotesPatientProxy;
import com.phildev.front.mls.service.NotePatientService;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest({FichePatientController.class, FichePatientService.class, NotePatientService.class})
public class FichePatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    @MockBean
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    @Test
    @WithMockUser
    @DisplayName("Test fiche patient avec calcul de l'âge du patient")
    public void testFichePatientAvecCalculAgePatient() throws Exception {
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),1,"PHIL TEST","First note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient), PageRequest.of(0,1),1);

        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenReturn(patient);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPaqe(1,0)).thenReturn(notes);
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(view().name("fiche_patient"))
                .andExpect(content().string(containsString("<div class=\"fs-4 text-center mt-2\">Informations générales</div>")))
                .andExpect(content().string(containsString("<td>PHIL</td>")))
                .andExpect(content().string(containsString("<td>TEST</td>")))
                .andExpect(content().string(containsString("<td>34</td>")))
                .andExpect(content().string(containsString("<td>15 rue des tests</td>")))
                .andExpect(content().string(containsString("<td>000-555-9999</td>")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Test message d'erreur sur fiche patient si le service proxy renvoie une erreur")
    public void testFichePatientAvecErreurSiProxyRenvoieUneErreur() throws Exception {
        when(microserviceCoordonneesPatientProxy.recuperePatient(1L)).thenThrow(ResponseNotFoundException.class);
        mockMvc.perform(get("/patient/fiche/1/pageNo/0"))
                .andDo(print())
                .andExpect(model().attributeExists("patientErreur"))
                .andExpect(content().string(containsString("Le patient n&#39;a pas été trouvé avec son id 1")))
                .andExpect(status().isOk());
    }
}
