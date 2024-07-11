package com.phildev.mls.donnees.patient.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phildev.mls.donnees.patient.model.CoordonneesPatient;
import com.phildev.mls.donnees.patient.service.CoordonneesPatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoordonneesPatientController.class)
public class CoordonneesPatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoordonneesPatientService coordonneesPatientService;

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    @DisplayName(value = "Test recherche coordonnees patient pour la première page avec le numéro de structure valide 1 et une liste de 2 patients en retour")
    public void testAffichageDePageAvecStructureValide() throws Exception {
        PageImpl<CoordonneesPatient> page = new PageImpl<>(Arrays.asList(
                new CoordonneesPatient(2L,1,"Test", "Phil", LocalDate.of(1980, Month.APRIL,12),"M","",""),
                new CoordonneesPatient(1L,1,"Jones", "Tom", LocalDate.of(1980, Month.APRIL,12),"M","1 rue des tests","000-444-5555")
        ));
        when(coordonneesPatientService.getAllCoordonneesPatientByStructureId(1, 0)).thenReturn(page);
        mockMvc.perform(get("/coordonneesPatient/structure/1/page")
                        .param("pageNo", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.totalPages",is(1)))
                .andExpect(jsonPath("$.numberOfElements",is(2)))
                .andExpect(jsonPath("$.totalElements",is(2)))
                .andExpect(jsonPath("$.content[0].nom", is("Test")))
                .andExpect(jsonPath("$.content[1].nom", is("Jones")))
                .andExpect(jsonPath("$.content[1].telephone", is("000-444-5555")))
                .andExpect(jsonPath("$.content[0].dateDeNaissance", is("1980-04-12")));
    }

    @Test
    @DisplayName(value = "Test recherche coordonnees patient pour la deuxième page qui n'existe pas")
    public void testAffichagePage2NonExistante() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        PageImpl<CoordonneesPatient> page = new PageImpl<>(Arrays.asList(
                new CoordonneesPatient(2L,1,"Test", "Phil", LocalDate.of(1980, Month.APRIL,12),"M","",""),
                new CoordonneesPatient(1L,1,"Jones", "Tom", LocalDate.of(1980, Month.APRIL,12),"M","1 rue des tests","000-444-5555")
        ), pageable, 1);
        when(coordonneesPatientService.getAllCoordonneesPatientByStructureId(1, 1)).thenReturn(page);
        mockMvc.perform(get("/coordonneesPatient/structure/1/page")
                        .param("pageNo", "2"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("La page demandée n'existe pas car le nombre total de pages est de 1 pour la structure 1")));
    }

    @Test
    @DisplayName(value = "Test recherche coordonnees patient pour la première page avec infos nombre total elements 10 et nombre de pages 2")
    public void testAffichagePage1AvecInfosTotalElements10EtNbPages2() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        PageImpl<CoordonneesPatient> page = new PageImpl<>(Arrays.asList(
                new CoordonneesPatient(2L,1,"Test", "Phil", LocalDate.of(1980, Month.APRIL,12),"M","",""),
                new CoordonneesPatient(1L,1,"Jones", "Tom", LocalDate.of(1980, Month.APRIL,12),"M","1 rue des tests","000-444-5555")
        ), pageable, 10);
        when(coordonneesPatientService.getAllCoordonneesPatientByStructureId(1, 0)).thenReturn(page);
        mockMvc.perform(get("/coordonneesPatient/structure/1/page")
                        .param("pageNo", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.totalPages",is(2)))
                .andExpect(jsonPath("$.totalElements",is(10)))
                .andExpect(jsonPath("$.numberOfElements",is(2)));

    }


    @Test
    @DisplayName(value = "Test recherche coordonnees patient pour la première page renvoie null avec 404")
    public void testAffichageDePageRenvoie404() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientByStructureId(1, 0)).thenReturn(null);
        mockMvc.perform(get("/coordonneesPatient/structure/1/page")
                        .param("pageNo", "1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",is("Pas de patient trouvé sur la structure 1")));
    }


    @Test
    @DisplayName(value="Test recherche coordonnees patient avec id patient inexistant qui renvoie une exception")
    public void testCoordonneesPatientAvecPatientInconnu() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientById(3L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/coordonneesPatient/3"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Le patient avec l'id 3 n'a pas été trouvé")));
    }

    @Test
    @DisplayName(value="Test recherche coordonnees patient avec id patient null dans la requête")
    public void testCoordonneesPatientAvecPatientNull() throws Exception {
        mockMvc.perform(get("/coordonneesPatient/ "))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value="Test recherche coordonnees patient avec id patient valide dans la requête")
    public void testCoordonneesPatientAvecPatientValide() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientById(10L)).thenReturn(
                Optional.of(new CoordonneesPatient(10L, 2, "James", "Lebron", LocalDate.of(1990, 5, 24),"M", null, null)));
        mockMvc.perform(get("/coordonneesPatient/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom", is("Lebron")))
                .andExpect(jsonPath("$.dateDeNaissance", is("1990-05-24")));
    }

    @Test
    @DisplayName(value="Test recherche coordonnees patient avec nom/prenom patient valide dans la requête")
    public void testCoordonneesPatientAvecNomPrenomPatientValide() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientByNomEtPrenom("TEST", "TESTER")).thenReturn(
                new CoordonneesPatient(10L, 2, "TEST", "TESTER", LocalDate.of(1974, 6, 7),"M", null, null));
        mockMvc.perform(get("/coordonneesPatient?nom=Test&prenom=Tester"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom", is("TESTER")))
                .andExpect(jsonPath("$.dateDeNaissance", is("1974-06-07")));
    }

    @Test
    @DisplayName(value="Test recherche coordonnees patientavec nom/prenom patient vides dans la requête")
    public void testCoordonneesPatientAvecNomPrenomVides() throws Exception {
          mockMvc.perform(get("/coordonneesPatient?nom=&prenom="))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("le nom ne doit pas être nul ou vide")))
                .andExpect(jsonPath("$.message", containsString("le prénom ne doit pas être nul ou vide")));
    }

    @Test
    @DisplayName(value="Test recherche coordonnees patient par nom/prenom avec retour null qui renvoie une exception")
    public void testCoordonneesPatientAvecNomPrenomQuiRetourneException() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientByNomEtPrenom("Test", "Tester")).thenThrow(new RuntimeException(""));

        mockMvc.perform(get("/coordonneesPatient?nom=Test&prenom=Tester"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Aucun patient trouvé avec le nom Test et prénom Tester")));

    }

    @Test
    @DisplayName(value = "Test de création d un patient avec un objet valide")
    public void testCreationPatientValide() throws Exception {
        CoordonneesPatient patientValide = new CoordonneesPatient(11L,2,"VALIDE","PATIENT", LocalDate.of(1988,4,1),"M","","");
        when(coordonneesPatientService.save(patientValide)).thenReturn(patientValide);
        mockMvc.perform(post("/coordonneesPatient")
                .content(mapper.writeValueAsString(patientValide))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.nom", is("VALIDE")))
                .andExpect(status().isCreated());
    }
    @Test
    @DisplayName(value = "Test retour exception lors de la création d'un patient")
    public void testExceptionCreationPatientValide() throws Exception {
        CoordonneesPatient patientValide = new CoordonneesPatient(11L,2,"VALIDE","PATIENT", LocalDate.of(1988,4,1),"F","","");
        when(coordonneesPatientService.save(patientValide)).thenThrow(new RuntimeException(""));
        mockMvc.perform(post("/coordonneesPatient")
                        .content(mapper.writeValueAsString(patientValide))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Une erreur est survenue, le patient n'a pas été créé")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "Test création d'un patient avec objet invalide")
    public void testCreationPatientAvecObjetInvalide() throws Exception {
        CoordonneesPatient patientInValide = new CoordonneesPatient(11L,null,"","", null,"","","");

        mockMvc.perform(post("/coordonneesPatient")
                        .content(mapper.writeValueAsString(patientInValide))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message", containsString("dateDeNaissance: ne doit pas être vide ou nulle et doit respecter le format yyyy-MM-dd")))
                 .andExpect(jsonPath("$.message", containsString("structureId: ne doit pas être nul")))
                .andExpect(jsonPath("$.message", containsString("nom: ne doit pas être nul ou vide")))
                .andExpect(jsonPath("$.message", containsString("prenom: ne doit pas être nul ou vide")))
                .andExpect(jsonPath("$.message", containsString("genre: Le genre doit être M ou F pour masculin ou féminin")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test de suppression d un patient par son id invalide")
    public void testSuppressionAvecIdPatientInValide() throws Exception {
        when(coordonneesPatientService.findById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/coordonneesPatient/delete/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Pas de patient trouvé avec cet id : 2")));

    }

    @Test
    @DisplayName("Test de suppression d un patient par son id valide 2")
    public void testSuppressionAvecIdPatientValide() throws Exception {

        when(coordonneesPatientService.findById(2L)).thenReturn(
                Optional.of(new CoordonneesPatient(11L, 2, "VALIDE", "PATIENT", LocalDate.of(1981, 11, 1),"F", "", "")));

        mockMvc.perform(delete("/coordonneesPatient/delete/2"))
                .andExpect(jsonPath("$", is("Le patient avec l id 2 a bien été supprimé")));
    }

    @ParameterizedTest(name = "Test de récupération d un patient avec son nom {0} et prénom {1}")
    @CsvSource(value = {"Valide, Patient", "VALIDE, PATIENT", "vAliDE, PatienT"})
    public void testRecuperationPatientAvecNomPrenom(String nom, String prenom) throws Exception {
        CoordonneesPatient patientValide = new CoordonneesPatient(11L,2,"VALIDE","PATIENT", LocalDate.of(1988,4,1),"M","","");
        when(coordonneesPatientService.getAllCoordonneesPatientByNomEtPrenom("VALIDE", "PATIENT")).thenReturn(patientValide);
        mockMvc.perform(get("/coordonneesPatient")
                        .param("nom", nom)
                        .param("prenom", prenom))
                .andDo(print())
                .andExpect(jsonPath("$.nom", is("VALIDE")))
                .andExpect(jsonPath("$.prenom", is("PATIENT")))
                .andExpect(jsonPath("$.dateDeNaissance", is("1988-04-01")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test d'un patient non trouvé avec son nom prenom qui renvoie une 404")
    public void testRecuperationPatientAvecNomPrenomNonTrouve() throws Exception {
        when(coordonneesPatientService.getAllCoordonneesPatientByNomEtPrenom("VALIDE", "PATIENT")).thenReturn(null);
        mockMvc.perform(get("/coordonneesPatient")
                        .param("nom", "VALIDE")
                        .param("prenom", "PATIENT"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
