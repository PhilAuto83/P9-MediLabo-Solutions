package com.phildev.front.mls.service;

import com.phildev.front.mls.model.Structure;
import com.phildev.front.mls.repository.StructureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = StructureService.class)
@ActiveProfiles("test")
public class StructureServiceTest {

    @MockBean
    private StructureRepository structureRepository;

    @Autowired
    private StructureService structureService;


    @Test
    @DisplayName("Test récupération de la structure par son id")
    void testRecuperationStructureParId(){
        Optional<Structure> structure = Optional.of(new Structure(4, "Labo test"));
        when(structureRepository.findById(4)).thenReturn(structure);
        Assertions.assertEquals("Labo test", structureService.getStructureNameById(4));
    }

    @Test
    @DisplayName("Test récupération de la structure par son id avec retour null")
    void testRecuperationStructureNulle(){
        Optional<Structure> structure = Optional.empty();
        when(structureRepository.findById(4)).thenReturn(structure);
        Assertions.assertNull(structureService.getStructureNameById(4));
    }
}
