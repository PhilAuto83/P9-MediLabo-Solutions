package com.phildev.mls.diabete.service;

import com.phildev.mls.diabete.model.CoordonneesPatient;
import com.phildev.mls.diabete.model.NiveauDeRisque;
import com.phildev.mls.diabete.model.NotePatient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@SpringBootTest(classes = {DiabeteService.class})
class DiabeteServiceTest {

    @Autowired
    private DiabeteService diabeteService;

    @MockBean
    private MicroserviceCoordonneesPatientProxy microserviceCoordonneesPatientProxy;

    @MockBean
    private MicroserviceNotePatientProxy microserviceNotePatientProxy;

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque limité pour patient plus de 30 ans  et 3 déclencheurs")
    void testNiveauRisqueLimitePlus30ans3Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "PHIL TEST", "Poids fumeur");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "PHIL TEST", "Le patient a des vertiges");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "PHIL", LocalDate.of(1993,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.LIMITE.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque limité pour patient plus de 30 ans et 5 déclencheurs")
    void testNiveauRisqueLimitePlus30ans5Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "PHIL TEST", "Poids fumeur");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "PHIL TEST", "Le patient a des vertiges. Rechute sur le sucre avec anitcorps faible.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "PHIL", LocalDate.of(1993,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.LIMITE.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque danger pour patient plus de 30 ans et 6 déclencheurs")
    void testNiveauRisqueDangerPlus30ans6Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "PHIL TEST", "Microalbumine réaction Taille");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "PHIL TEST", "Le patient a des vertiges. Rechute sur le sucre avec anticorps faible.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "PHIL", LocalDate.of(1993,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.DANGER.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque danger pour patient homme - de 30 ans et 3 déclencheurs")
    void testNiveauRisqueDangerHommeMoins30ans3Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "PHIL TEST", "Hémoglobine A1C");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "PHIL TEST", "Le patient est fumeur et a du cholestérol.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "PHIL", LocalDate.of(1996,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.DANGER.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque danger pour patient homme - de 30 ans et 4 déclencheurs")
    void testNiveauRisqueDangerHommeMoins30ans4Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "PHIL TEST", "Hémoglobine A1C, RECHUTE");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "PHIL TEST", "Le patient est fumeur et a du cholestérol.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "PHIL", LocalDate.of(1996,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.DANGER.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque danger pour patient homme - de 30 ans et 4 déclencheurs")
    void testNiveauRisqueDangerFemmeMoins30ans4Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEANNA TEST", "Taux anormal Hémoglobine A1C");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEANNA TEST", "La patiente est fumeuse et a un poids élevé.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEANNA", LocalDate.of(1996,7,21), "F", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.DANGER.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec niveau risque danger pour patient femme - de 30 ans et 6 déclencheurs")
    void testNiveauRisqueDangerFemmeMoins30ans6Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEANNA TEST", "Taux anormal Hémoglobine A1C, anticorps + vertiges");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEANNA TEST", "La patiente est fumeuse et a un poids élevé.");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEANNA", LocalDate.of(1996,7,21), "F", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.DANGER.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec femme aucun risque pour 1 déclencheur")
    void testNiveauRisqueNoneAvec1Declencheur(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEANNA TEST", "Taux normal Hémoglobine A1C");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEANNA TEST", "La patiente est en forme");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEANNA", LocalDate.of(1996,7,21), "F", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.AUCUN.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque avec homme  aucun risque pour 0 déclencheur")
    void testNiveauRisqueNoneAvec0Declencheur(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEO TEST", "Taux normal de sucre");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEO TEST", "Le patient est en forme");
        List<NotePatient> notes = List.of(note1, note2);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEO", LocalDate.of(1996,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.AUCUN.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque early on set avec homme plus de 30 ans pour 8 déclencheurs")
    void testNiveauRisqueEarlyOnSetPlus30ansAvec8Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEO TEST", "Taux normal de sucre élevé, Hémoglobine A1C elevée.");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEO TEST", "Le patient est fumeur, a des vertiges et a fait une rechute. réaction allergique au nutella");
        NotePatient note3 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEO TEST", "Le patient est en surcharge anormale. Le poids n'est pas bon avec taille moyenne.");
        List<NotePatient> notes = List.of(note1, note2, note3);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEO", LocalDate.of(1980,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.PRECOCE.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque early on set avec homme moins de 30 ans pour 5 déclencheurs")
    void testNiveauRisqueEarlyOnSetMoins30ansAvec5Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEO TEST", "Taux normal de sucre élevé, Hémoglobine A1C elevée.");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEO TEST", "Le patient est fumeur, a des vertiges.");
        NotePatient note3 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEO TEST", "Le patient est en surcharge anormale. Le poids n'est pas bon.");
        List<NotePatient> notes = List.of(note1, note2, note3);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEO", LocalDate.of(1999,7,21), "M", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.PRECOCE.toString(), diabeteService.calculNiveauRisque(5));
    }

    @Test
    @DisplayName("test de la méthode calcul niveau risque early on set avec femme moins de 30 ans pour 7 déclencheurs")
    void testNiveauRisqueEarlyOnSetFemmeMoins30ansAvec7Declencheurs(){
        NotePatient note1 = new NotePatient("123456d", LocalDateTime.now(), 5, "LEA TEST", "Taux normal de sucre élevé avec cholestérol, Hémoglobine A1C elevée.");
        NotePatient note2 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEA TEST", "La patiente est fumeuse, a des vertiges et rechute.");
        NotePatient note3 = new NotePatient("213456j", LocalDateTime.now().minusDays(5), 5, "LEA TEST", "La patiente est en  pondérale anormale. Le poids n'est pas bon.");
        List<NotePatient> notes = List.of(note1, note2, note3);
        CoordonneesPatient patient = new CoordonneesPatient(5L, 2, "TEST", "LEA", LocalDate.of(1995,7,21), "F", "", "");
        when(microserviceNotePatientProxy.recupererLesNotesParPatient(5)).thenReturn(notes);
        when(microserviceCoordonneesPatientProxy.recuperePatient(5L)).thenReturn(patient);
        Assertions.assertEquals(NiveauDeRisque.PRECOCE.toString(), diabeteService.calculNiveauRisque(5));
    }
}
