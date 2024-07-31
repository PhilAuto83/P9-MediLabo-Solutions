package com.phildev.front.mls.service;

import com.phildev.front.mls.error.FichePatientNotFoundException;
import com.phildev.front.mls.model.CoordonneesPatient;
import com.phildev.front.mls.model.FichePatient;
import com.phildev.front.mls.model.NotePatient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest(classes = FichePatientService.class)
class FichePatientServiceTest {

    @Autowired
    private FichePatientService fichePatientService;

    @MockBean
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    @MockBean
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    @Test
    @DisplayName("Test d'ajout du note patient")
    void testAjoutNotePatient(){
        NotePatient note  = new NotePatient("123456t", LocalDateTime.now(),2, "PHIL TEST","note 1");
        when(microserviceNotesPatientProxy.ajouterUneNote(note)).thenReturn(note);
        NotePatient noteAjoutee = fichePatientService.ajouterUneNote(note);
        Assertions.assertEquals("PHIL TEST", noteAjoutee.getPatient());
        Assertions.assertEquals(2, noteAjoutee.getPatientId());
        Assertions.assertEquals("123456t", noteAjoutee.getId());
        Assertions.assertEquals("note 1", noteAjoutee.getNote());
    }

    @Test
    @DisplayName("Test affichage fiche patient")
    void testAffichageFichePatient(){
        CoordonneesPatient patient = new CoordonneesPatient(1000L, 2, "TEST", "PHIL", LocalDate.of(1990,5,21),  "M", "15 rue des tests", "000-555-9999");
        when(microserviceCoordonneesPatientProxy.recuperePatient(1000L)).thenReturn(patient);
        FichePatient fiche = fichePatientService.recupereLaFichePatient(1000L);
        Assertions.assertEquals("TEST",fiche.getNom());
        Assertions.assertEquals("PHIL",fiche.getPrenom());
        Assertions.assertEquals("15 rue des tests",fiche.getAdresse());
        Assertions.assertEquals(34,fiche.getAge());
        Assertions.assertEquals(2,fiche.getStructureId());
        Assertions.assertNotNull(fiche.getId());
        Assertions.assertEquals("000-555-9999",fiche.getTelephone());
    }

    @Test
    @DisplayName("Test données patient non trouvée avec une FichePatientNotFoundException")
    void testFichePatientNonTrouvee(){
        when(microserviceCoordonneesPatientProxy.recuperePatient(1000L)).thenThrow(FichePatientNotFoundException.class);
        Assertions.assertThrows(FichePatientNotFoundException.class, ()->fichePatientService.recupereLaFichePatient(1000L));
    }
}
