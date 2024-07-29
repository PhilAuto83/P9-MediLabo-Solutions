package com.phildev.front.mls.service;


import com.phildev.front.mls.error.PatientExistantException;
import com.phildev.front.mls.error.ResponseNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.NotePatient;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = PatientService.class)
public class PatientServiceTest {

    @MockBean
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PatientService patientService;


    @Test
    @DisplayName("Test récupération des coordonnées des patients sur la structure de l'utilisateur")
    void testRecuperationListeCoordonneesParStructure(){
        Principal principal = () -> "test@test.fr";
        when(userRepository.findByEmail("test@test.fr")).thenReturn(new User(10, "Testee", "Tester", 2, "USER", "test@test.fr", "ffqdfsd"));
        when(microserviceCoordonneesPatientProxy.recupereCoordonneesParStructure(2)).thenReturn(List.of(new CoordonneesPatient(1L, 2, "Louis", "Joe", LocalDate.of(1990,10,12), "M","12 rue des tests","000-555-1478"),
                new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","118 rue des tests","456-555-1478")));
        List<CoordonneesPatient> coordonneesPatients = patientService.recupereToutesLesCoordonneesPatient(principal);
        Assertions.assertEquals("000-555-1478", coordonneesPatients.getFirst().getTelephone());
        Assertions.assertEquals("Joe", coordonneesPatients.getFirst().getPrenom());
        Assertions.assertEquals("Louis", coordonneesPatients.getFirst().getNom());
        Assertions.assertEquals("12 rue des tests", coordonneesPatients.getFirst().getAdresse());
        Assertions.assertEquals(2, coordonneesPatients.getFirst().getStructureId());
        Assertions.assertEquals(LocalDate.of(1990,10,12), coordonneesPatients.getFirst().getDateDeNaissance());
        Assertions.assertEquals("456-555-1478", coordonneesPatients.getLast().getTelephone());
        Assertions.assertEquals("Tyron", coordonneesPatients.getLast().getPrenom());
        Assertions.assertEquals("Lue", coordonneesPatients.getLast().getNom());
        Assertions.assertEquals("M", coordonneesPatients.getLast().getGenre());
        Assertions.assertEquals(2, coordonneesPatients.getLast().getStructureId());
        Assertions.assertEquals(LocalDate.of(1979,4,21), coordonneesPatients.getLast().getDateDeNaissance());
    }

    @Test
    @DisplayName("Test erreur feign sur l'appel au backend coordonnées patient renvoie une liste vide")
    void testErreurFeignAppelCoordonneesPatientBackEnd(){
        Principal principal = () -> "test@test.fr";
        when(userRepository.findByEmail("test@test.fr")).thenReturn(new User(10, "Testee", "Tester", 2, "USER", "test@test.fr", "ffqdfsd"));
        when(microserviceCoordonneesPatientProxy.recupereCoordonneesParStructure(2)).thenThrow(FeignException.class);
        Assertions.assertEquals(Collections.emptyList(), patientService.recupereToutesLesCoordonneesPatient(principal));
    }

    @Test
    @DisplayName("Test suppression d'un patient avec ses coordonnées avec erreur 400 côté back end")
    void testSuppressionPatientAvecCoordonneesRenvoie400(){
        when(microserviceCoordonneesPatientProxy.supprimerPatient(100L)).thenReturn(ResponseEntity.badRequest().body("Bad request"));
        Assertions.assertThrows(RuntimeException.class, ()->patientService.supprimerPatient(100L));
    }

    @Test
    @DisplayName("Test ajout nouveau patient non existant")
    void testAjoutPatientNonExistant(){
        CoordonneesPatient patient = new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","118 rue des tests","456-555-1478");
        when(microserviceCoordonneesPatientProxy.sauvegarderUnPatient(patient)).thenReturn(patient);
        when(microserviceCoordonneesPatientProxy.recuperePatientParNomPrenom("Lue", "Tyron")).thenThrow(ResponseNotFoundException.class);
        CoordonneesPatient patientSaved = patientService.sauvegarderUnPatient(patient);
        org.assertj.core.api.Assertions.assertThat(patientSaved.getStructureId()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(patientSaved.getId()).isEqualTo(156789L);
        org.assertj.core.api.Assertions.assertThat(patientSaved.getNom()).isEqualTo("Lue");
        org.assertj.core.api.Assertions.assertThat(patientSaved.getDateDeNaissance()).isEqualTo(LocalDate.of(1979,4,21));
        org.assertj.core.api.Assertions.assertThat(patientSaved.getTelephone()).isEqualTo("456-555-1478");
    }

    @Test
    @DisplayName("Test ajout nouveau patient existant")
    void testAjoutPatientExistant(){
        CoordonneesPatient patient = new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","118 rue des tests","456-555-1478");
        when(microserviceCoordonneesPatientProxy.recuperePatientParNomPrenom("Lue", "Tyron")).thenReturn(patient);
        Assertions.assertThrows(PatientExistantException.class, ()->patientService.sauvegarderUnPatient(patient));
    }

    @Test
    @DisplayName("Test mis à jour patient sans changer de nom prenom")
    void testMisAJourPatientSaufNomPrenom(){
        CoordonneesPatient patientInDB = new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","118 rue des tests","456-555-1478");
        CoordonneesPatient patientNewData = new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","10 rue des tests","123-555-1478");
        when(microserviceCoordonneesPatientProxy.recuperePatient(156789L)).thenReturn(patientInDB);
        when(microserviceCoordonneesPatientProxy.sauvegarderUnPatient(patientNewData)).thenReturn(patientNewData);
        CoordonneesPatient patientUpdated = patientService.miseAJourPatient(patientNewData);
        Assertions.assertEquals("123-555-1478", patientUpdated.getTelephone());
        Assertions.assertEquals("10 rue des tests", patientUpdated.getAdresse());
        Assertions.assertEquals("Lue", patientUpdated.getNom());
        Assertions.assertEquals("Tyron", patientUpdated.getPrenom());
        Assertions.assertEquals(156789L, patientUpdated.getId());
    }

    @Test
    @DisplayName("Test mis à jour patient avec changement de nom prénom existant avec un autre id en base")
    void testMisAJourPatientAvecChangementNomPrenomExistantAvecAutreId(){
        CoordonneesPatient patientInDB = new CoordonneesPatient(156789L, 2, "Li", "Jet", LocalDate.of(1979,4,21), "M","118 rue des tests","456-555-1478");
        CoordonneesPatient patientNewData = new CoordonneesPatient(156789L, 2, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","10 rue des tests","123-555-1478");
        CoordonneesPatient patientExistant = new CoordonneesPatient(10L, 8, "Lue", "Tyron", LocalDate.of(1979,4,21), "M","10 rue des tests","123-555-1478");
        when(microserviceCoordonneesPatientProxy.recuperePatient(156789L)).thenReturn(patientInDB);
        when(microserviceCoordonneesPatientProxy.recuperePatientParNomPrenom("Lue", "Tyron")).thenReturn(patientExistant);
        Assertions.assertThrows(PatientExistantException.class, ()->patientService.miseAJourPatient(patientNewData));
    }

    @Test
    @DisplayName("récupération des coordonnées d'un patient via son id")
    void testRecuperationCoordonneesPatientViaId(){
        CoordonneesPatient patientInDB = new CoordonneesPatient(10L, 2, "Flessel", "Laura", LocalDate.of(1979,4,21), "F","118 rue des tests","456-555-1478");
        when(microserviceCoordonneesPatientProxy.recuperePatient(10L)).thenReturn(patientInDB);
        CoordonneesPatient patient = patientService.recuperePatient(10L);
        Assertions.assertEquals("Laura", patient.getPrenom());
        Assertions.assertEquals("Flessel", patient.getNom());
        Assertions.assertEquals(10L, patient.getId());
        Assertions.assertEquals(2, patient.getStructureId());
        Assertions.assertEquals(LocalDate.of(1979,4,21), patient.getDateDeNaissance());
        Assertions.assertEquals("F", patient.getGenre());
    }

    @Test
    @DisplayName("récupération des coordonnées d'un patient avec pagination")
    void testRecuperationCoordonneesPatientAvecPagination(){
        Principal principal = () -> "test@test.fr";
        when(userRepository.findByEmail("test@test.fr")).thenReturn(new User(10, "Testee", "Tester", 2, "USER", "test@test.fr", "ffqdfsd"));
        CoordonneesPatient patient = new CoordonneesPatient(1L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        CoordonneesPatient patient2 = new CoordonneesPatient(2L, 2, "TESTER", "LEANNA", LocalDate.of(2000,5,21),  "F", "17 rue des tests", "111-555-9999");

        Page<CoordonneesPatient> coordonneesPatients = new PageImpl<>(List.of(patient, patient2), PageRequest.of(0,2),1);
        when(microserviceCoordonneesPatientProxy.recupereCoordonneesParStructureAvecPagination(2, 0)).thenReturn(coordonneesPatients);
        Page<CoordonneesPatient> coordonneesPatientsTrouves = patientService.recupereToutesLesCoordonneesPatientAvecPagination(principal, 0);
        Assertions.assertEquals(2, coordonneesPatientsTrouves.getTotalElements());
        Assertions.assertEquals(2, coordonneesPatientsTrouves.getContent().size());
        Assertions.assertEquals("M", coordonneesPatientsTrouves.getContent().getFirst().getGenre());
        Assertions.assertEquals("F", coordonneesPatientsTrouves.getContent().getLast().getGenre());
        Assertions.assertEquals("TEST", coordonneesPatientsTrouves.getContent().getFirst().getNom());
        Assertions.assertEquals("PHIL", coordonneesPatientsTrouves.getContent().getFirst().getPrenom());
        Assertions.assertEquals("TESTER", coordonneesPatientsTrouves.getContent().getLast().getNom());
        Assertions.assertEquals("LEANNA", coordonneesPatientsTrouves.getContent().getLast().getPrenom());
    }

    @Test
    @DisplayName("Coordonnées d'un patient non trouvée")
    void testCoordonneesPatientNonTrouvee(){
        Principal principal = () -> "test@test.fr";
        when(userRepository.findByEmail("test@test.fr")).thenReturn(new User(10, "Testee", "Tester", 2, "USER", "test@test.fr", "ffqdfsd"));
        when(microserviceCoordonneesPatientProxy.recupereCoordonneesParStructureAvecPagination(2, 0)).thenThrow(ResponseNotFoundException.class);
        Assertions.assertThrows(ResponseNotFoundException.class, ()->patientService.recupereToutesLesCoordonneesPatientAvecPagination(principal, 0));
    }
}
