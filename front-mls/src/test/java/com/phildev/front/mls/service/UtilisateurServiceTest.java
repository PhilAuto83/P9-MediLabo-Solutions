package com.phildev.front.mls.service;


import com.phildev.front.mls.dto.UserDTO;
import com.phildev.front.mls.model.Structure;
import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.StructureRepository;
import com.phildev.front.mls.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = UtilisateurService.class)
@ActiveProfiles("test")
public class UtilisateurServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private StructureRepository structureRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Test
    @DisplayName("Test affichage des utilisateurs et leur structure associée")
    void testAffichageUtilisateursEtStructureAssociee(){
        when(userRepository.findAll()).thenReturn(List.of(new User(7,"Test", "Tessss",4,"USER", "test@email.fr", "dfqdfdf46dsfdfq&"),
                new User(8,"Testee", "Test",9,"USER", "testee@email.fr", "dfqdfdf46dsfdfq&")));
        when(structureRepository.findAll()).thenReturn(List.of(new Structure(4, "Labo 1"), new Structure(10, "Doc Sam Cassell"), new Structure(9, "Doc B.B King")));
        List<UserDTO> users = utilisateurService.afficheUtilisateurs();
        Assertions.assertEquals("Labo 1", users.getFirst().getStructureName());
        Assertions.assertEquals("Test", users.getFirst().getFirstname());
        Assertions.assertEquals("Doc B.B King", users.getLast().getStructureName());
        Assertions.assertEquals("Testee", users.getLast().getFirstname());
    }
}
