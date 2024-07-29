package com.phildev.front.mls.service;

import com.phildev.front.mls.model.NotePatient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = NotePatientService.class)
public class NotePatientServiceTest {

    @MockBean
    private MicroserviceNotesPatientProxy microserviceNotesPatientProxy;

    @Autowired
    private NotePatientService notePatientService;

    @Test
    @DisplayName("Test récupération des notes avec pages")
    void testRecuperationNotesAVecPages(){
        NotePatient notePatient  = new NotePatient("123456a", LocalDateTime.now(),2,"PHIL TEST","First note");
        NotePatient notePatient2  = new NotePatient("123456b", LocalDateTime.now().minusDays(10),2,"PHIL TEST","Second note");
        Page<NotePatient> notes = new PageImpl<>(List.of(notePatient, notePatient2), PageRequest.of(1,2),1);
        when(microserviceNotesPatientProxy.recupererLesNotesParPatientParPage(2, 1)).thenReturn(notes);
        Page<NotePatient> notesTrouvees = notePatientService.recupererLesNotesParPatient(2, 1);
        Assertions.assertEquals("First note", notesTrouvees.getContent().getFirst().getNote());
        Assertions.assertEquals("Second note", notesTrouvees.getContent().getLast().getNote());
        Assertions.assertEquals(2, notesTrouvees.getContent().getLast().getPatientId());
        Assertions.assertEquals("PHIL TEST", notesTrouvees.getContent().getFirst().getPatient());
        Assertions.assertEquals("123456a", notesTrouvees.getContent().getFirst().getId());
        Assertions.assertTrue(notesTrouvees.getContent().getFirst().getDateCreation().isAfter(notesTrouvees.getContent().getLast().getDateCreation()));
    }
}
